package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static ru.practicum.shareit.item.mapper.CommentMapper.toComment;
import static ru.practicum.shareit.item.mapper.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toExtendedItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

/**
 * Сервис по работе с вещами
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserService userService,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.checkUserExist(userId);
        Item item = toItem(itemDto);
        User user = userRepository.findById(userId).get();
        item.setOwner(user);
        Item savedItem = itemRepository.save(item);
        return toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        userService.checkUserExist(userId);

        Item savedItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NoSuchElementException("Item not exist"));

        if (savedItem.getOwner().getId().compareTo(userId) != 0) {
            throw new NoSuchElementException("Item not exist");
        }
        if (itemDto.getAvailable() != null) {
            savedItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            savedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            savedItem.setDescription(itemDto.getDescription());
        }
        Item updatedItem = itemRepository.save(savedItem);
        return toItemDto(updatedItem);
    }

    @Override
    public ExtendedItemDto getItem(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not exist"));

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NoSuchElementException("Item not exist"));

        List<CommentDto> comments = commentRepository.findAllByItem_Id(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        var nextBooking = bookingRepository.findTopByItem_IdAndItem_OwnerAndStartAfterOrderByStartAsc(itemId, user, now());
        var lastBooking = bookingRepository.findTopByItem_IdAndItem_OwnerAndEndBeforeOrderByEndDesc(itemId, user, now());

        return toExtendedItemDto(item, nextBooking, lastBooking, comments);
    }

    @Override
    public Collection<ExtendedItemDto> getItemsByOwner(Long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not exist"));

        Page<Item> items = itemRepository.findAllByOwnerOrderById(user, PageRequest.of(from / size, size));

        LocalDateTime currentTime = now();
        List<ExtendedItemDto> itemDtoList = new ArrayList<>();
        for (Item i : items) {
            itemDtoList.add(
                    toExtendedItemDto(
                            i,
                            bookingRepository.findTopByItem_IdAndItem_OwnerAndStartAfterOrderByStartAsc(i.getId(), user, currentTime),
                            bookingRepository.findTopByItem_IdAndItem_OwnerAndEndBeforeOrderByEndDesc(i.getId(), user, currentTime),
                            commentRepository.findAllByItem_Id(i.getId())
                                    .stream()
                                    .map(CommentMapper::toCommentDto)
                                    .collect(Collectors.toList())));
        }
        return itemDtoList;
    }

    @Override
    public Collection<ItemDto> findItem(Long userId, String text, int from, int size) {
        userService.checkUserExist(userId);
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByText(text.toLowerCase(), PageRequest.of(from / size, size))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not exist"));

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NoSuchElementException("Item not exist"));

        boolean isAllowedToCreateComment = bookingRepository.existsByBooker_IdAndItem_IdAndEndBefore(userId, itemId, now());
        if (!isAllowedToCreateComment) {
            throw new IllegalArgumentException("User can't leave comment for this item");
        }
        Comment savedComment = commentRepository.save(toComment(comment, user, item, now()));
        return toCommentDto(savedComment);
    }
}
