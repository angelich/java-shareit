package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void saveNewItem() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .build();

        when(itemService.createItem(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .build();

        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void getItem() throws Exception {
        ExtendedItemDto itemDto = ExtendedItemDto.builder()
                .id(1L)
                .name("itemName")
                .description("desc")
                .available(true)
                .build();

        when(itemService.getItem(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void getOwnerItems() throws Exception {
        ExtendedItemDto itemDto1 = ExtendedItemDto.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .available(true)
                .build();

        ExtendedItemDto itemDto2 = ExtendedItemDto.builder()
                .id(1L)
                .name("name 2")
                .description("desc 2")
                .available(false)
                .build();

        List<ExtendedItemDto> dtos = List.of(itemDto1, itemDto2);

        when(itemService.getItemsByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(dtos);

        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(dtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDto1.getAvailable())))

                .andExpect(jsonPath("$.[1].name", is(itemDto2.getName())))
                .andExpect(jsonPath("$.[1].description", is(itemDto2.getDescription())))
                .andExpect(jsonPath("$.[1].available", is(itemDto2.getAvailable())));
    }

    @Test
    void searchItems() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .build();

        List<ItemDto> dtos = List.of(itemDto);

        when(itemService.findItem(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(dtos);

        mvc.perform(get("/items/search")
                        .content(mapper.writeValueAsString(dtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", itemDto.getDescription())
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void saveItemComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .authorName("userName")
                .text("so good item")
                .created(LocalDateTime.now())
                .build();

        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }
}
