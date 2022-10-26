package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemIntegrationTest {
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void shouldCreateItem() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mail.com");
        userDto = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto(1L, "itemName", "itemDesc", true, null);

        var createdItem = itemService.createItem(userDto.getId(), itemDto);

        assertThat(createdItem.getId(), notNullValue());
        assertThat(createdItem.getName(), equalTo(itemDto.getName()));
        assertThat(createdItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(createdItem.getAvailable(), equalTo(itemDto.getAvailable()));
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
