package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemIntegrationTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void shouldCreateItem() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mail.com");
        userDto = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto(1L, "itemName", "itemDesc", true, null);

        itemService.createItem(userDto.getId(), itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query
                .setParameter("id", userDto.getId())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    void shouldReturnUserItem() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mail.com");
        userDto = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto(1L, "itemName", "itemDesc", true, null);

        itemService.createItem(userDto.getId(), itemDto);

        var items = itemService.getItemsByOwner(userDto.getId(), 0, 10);
        var item = items.stream().findFirst().get();

        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }
}
