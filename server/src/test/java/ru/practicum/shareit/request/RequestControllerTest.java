package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class RequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldCreateRequest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .available(true)
                .build();

        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(List.of(itemDto))
                .description("desc")
                .build();

        when(requestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", notNullValue()));
    }

    @Test
    void shouldReturnUserRequests() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .available(true)
                .build();

        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(List.of(itemDto))
                .description("desc")
                .build();

        when(requestService.getMyRequests(anyLong()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", notNullValue()))
                .andExpect(jsonPath("$.[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.[0].created", notNullValue()));
    }

    @Test
    void shouldReturnAllRequests() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .available(true)
                .build();

        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(List.of(itemDto))
                .description("desc")
                .build();

        when(requestService.getAllRequests(anyLong(), any(), any()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", notNullValue()))
                .andExpect(jsonPath("$.[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.[0].created", notNullValue()));
    }

    @Test
    void shouldReturnOneRequest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .available(true)
                .build();

        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(List.of(itemDto))
                .description("desc")
                .build();

        when(requestService.getOneRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", notNullValue()));
    }
}
