package ru.practicum.shareit.item;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    private ItemService itemService;

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(
                itemRepository,
                userService,
                userRepository,
                bookingRepository,
                commentRepository);
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void shouldCreateItem() {
        ItemDto dto = new ItemDto();
        dto.setName("itemName");
        dto.setDescription("itemDesc");
        dto.setAvailable(true);

        User user = new User(1L, "userName", "email@email.com");

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.save(any(Item.class)))
                .thenReturn(new Item(1L, "itemName", "itemDesc", true, user, null));

        ItemDto savedItem = itemService.createItem(user.getId(), dto);

        assertEquals(1L, savedItem.getId());
        assertEquals("itemName", savedItem.getName());
        assertEquals("itemDesc", savedItem.getDescription());
        assertEquals(true, savedItem.getAvailable());
    }

    @Test
    void shouldUpdateItem() {
        User user = new User(1L, "userName", "email@email.com");

        Item item = new Item(1L, "itemName", "itemDesc", true, user, null);

        Item itemForUpdate = new Item(1L, "itemNameUpdated", "itemDescUpdated", false, user, null);

        ItemDto itemDtoForUpdate = new ItemDto(1L, "itemNameUpdated", "itemDescUpdated", false, null);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(itemRepository.save(any(Item.class)))
                .thenReturn(itemForUpdate);

        var updatedItem = itemService.updateItem(user.getId(), itemDtoForUpdate, itemDtoForUpdate.getId());

        assertEquals(1L, updatedItem.getId());
        assertEquals("itemNameUpdated", updatedItem.getName());
        assertEquals("itemDescUpdated", updatedItem.getDescription());
        assertEquals(false, updatedItem.getAvailable());
    }

    @Test
    void shouldReturnItem() {
        User user = new User(1L, "userName", "email@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, user, null);

        Booking booking = Booking.builder()
                .id(1L)
                .booker(user).status(BookingStatus.APPROVED)
                .start(now().plusMinutes(1L))
                .end(now().plusDays(1L))
                .build();

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(bookingRepository.findTopByItem_IdAndItem_OwnerAndStartAfterOrderByStartAsc(anyLong(), any(), any()))
                .thenReturn(booking);

        when(bookingRepository.findTopByItem_IdAndItem_OwnerAndEndBeforeOrderByEndDesc(anyLong(), any(), any()))
                .thenReturn(booking);

        ExtendedItemDto savedItem = itemService.getItem(user.getId(), item.getId());

        assertEquals(1L, savedItem.getId());
        assertEquals("itemName", savedItem.getName());
        assertEquals("itemDesc", savedItem.getDescription());
        assertEquals(true, savedItem.getAvailable());
    }

    @Test
    void shouldReturnItemsByOwner() {
        User user = new User(1L, "userName", "email@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, user, null);

        var page = new PageImpl<>(List.of(item));

        Booking booking = Booking.builder()
                .id(1L)
                .booker(user).status(BookingStatus.APPROVED)
                .start(now().plusMinutes(1L))
                .end(now().plusDays(1L))
                .build();

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findAllByOwner(any(), any()))
                .thenReturn(page);

        when(bookingRepository.findTopByItem_IdAndItem_OwnerAndStartAfterOrderByStartAsc(anyLong(), any(), any()))
                .thenReturn(booking);

        when(bookingRepository.findTopByItem_IdAndItem_OwnerAndEndBeforeOrderByEndDesc(anyLong(), any(), any()))
                .thenReturn(booking);

        var ownerItem = itemService.getItemsByOwner(user.getId(), 0, 10)
                .stream().findFirst().get();

        assertEquals(1L, ownerItem.getId());
        assertEquals("itemName", ownerItem.getName());
        assertEquals("itemDesc", ownerItem.getDescription());
        assertEquals(true, ownerItem.getAvailable());
    }

    @Test
    void shouldFindItem() {
        User user = new User(1L, "userName", "email@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, user, null);

        var page = new PageImpl<>(List.of(item));

        when(itemRepository.findAllByText(any(), any()))
                .thenReturn(page);

        var foundItem = itemService.findItem(user.getId(), item.getDescription(), 0, 10)
                .stream().findFirst().get();

        assertEquals(1L, foundItem.getId());
        assertEquals("itemName", foundItem.getName());
        assertEquals("itemDesc", foundItem.getDescription());
        assertEquals(true, foundItem.getAvailable());
    }

    @Test
    void shouldCreateComment() {
        User user = new User(1L, "userName", "email@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, user, null);

        Comment comment = Comment.builder()
                .id(1L)
                .item(item)
                .text("some text")
                .created(now())
                .author(user)
                .build();

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(bookingRepository.existsByBooker_IdAndItem_IdAndEndBefore(anyLong(), any(), any()))
                .thenReturn(true);

        when(commentRepository.save(any()))
                .thenReturn(comment);

        var savedComment = itemService.createComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment));

        assertEquals(1L, savedComment.getId());
        assertEquals(comment.getText(), savedComment.getText());
        assertEquals(user.getName(), savedComment.getAuthorName());
    }

    @Test
    void shouldThrowIfItemNotFound() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.updateItem(1L, new ItemDto(), 1L)
        );
        assertEquals("Item not exist", exception.getMessage());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.getItem(1L, 1L)
        );
        assertEquals("User not exist", exception.getMessage());
    }

    @Test
    void shouldThrowIfItemNotFoundWhileGet() {
        User user = new User(1L, "userName", "email@email.com");

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.getItem(1L, 1L)
        );
        assertEquals("Item not exist", exception.getMessage());
    }

    @Test
    void shouldThrowIfUserNotFoundGetOwnerItems() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemService.getItemsByOwner(1L, 0, 10)
        );
        assertEquals("User not exist", exception.getMessage());
    }
}
