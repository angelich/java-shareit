package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private Item item1;
    private Item item2;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(new User(1L, "name", "email@email.ru"));
        item1 = itemRepository.save(new Item(1L, "name 1", "desc 1", true, user, null));
        item2 = itemRepository.save(new Item(2L, "name 2", "desc 2", true, user, 1L));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findByText() {
        Page<Item> items = itemRepository.findAllByText("name 2", Pageable.unpaged());
        Item foundItem = items.stream().findFirst().get();

        assertNotNull(foundItem.getId());
        assertEquals("name 2", foundItem.getName());
        assertEquals("desc 2", foundItem.getDescription());
    }

    @Test
    void findByRequestId() {
        List<Item> items = itemRepository.findAllByRequestId(1L);
        Item foundItem = items.stream().findFirst().get();
        assertNotNull(foundItem.getId());
        assertEquals("name 2", foundItem.getName());
        assertEquals("desc 2", foundItem.getDescription());
    }
}
