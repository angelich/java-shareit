package ru.practicum.shareit.item;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        userRepository.save(user);

        Mockito
                .when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.save(ArgumentMatchers.any(Item.class)))
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
        userRepository.save(user);

        Item item = new Item(1L, "itemName", "itemDesc", true, user, null);

        Item itemForUpdate = new Item(1L, "itemNameUpdated", "itemDescUpdated", false, user, null);

        ItemDto itemDtoForUpdate = new ItemDto(1L, "itemNameUpdated", "itemDescUpdated", false, null);

        Mockito
                .when(itemRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(itemRepository.save(ArgumentMatchers.any(Item.class)))
                .thenReturn(itemForUpdate);

        var updatedItem = itemService.updateItem(user.getId(), itemDtoForUpdate, itemDtoForUpdate.getId());

        assertEquals(1L, updatedItem.getId());
        assertEquals("itemNameUpdated", updatedItem.getName());
        assertEquals("itemDescUpdated", updatedItem.getDescription());
        assertEquals(false, updatedItem.getAvailable());
    }
}
