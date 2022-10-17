package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldBookItem() throws Exception {
        Item item = Item.builder()
                .id(1L)
                .name("itemName")
                .description("itemDesc")
                .available(true)
                .build();

        User booker = User.builder()
                .id(2L)
                .name("userName")
                .email("email@user.com")
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .id(1L)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(1L))
                .end(LocalDateTime.now().plusDays(1L))
                .status(BookingStatus.WAITING)
                .booker(booker)
                .build();

        when(bookingService.bookItem(anyLong(), any()))
                .thenReturn(bookingResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(bookingResponse.getId().intValue())))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().toString())))
                .andExpect(jsonPath("$.item.id", equalTo(bookingResponse.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", equalTo(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.item.description", equalTo(bookingResponse.getItem().getDescription())))
                .andExpect(jsonPath("$.booker.name", equalTo(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", equalTo(bookingResponse.getBooker().getEmail())));
    }

    @Test
    void shouldApproveBooking() throws Exception {
        Item item = Item.builder()
                .id(1L)
                .name("itemName")
                .description("itemDesc")
                .available(true)
                .build();

        User booker = User.builder()
                .id(2L)
                .name("userName")
                .email("email@user.com")
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .id(1L)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(1L))
                .end(LocalDateTime.now().plusDays(1L))
                .status(BookingStatus.APPROVED)
                .booker(booker)
                .build();

        when(bookingService.approveBooking(anyLong(), anyLong(), any()))
                .thenReturn(bookingResponse);

        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(bookingResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(bookingResponse.getId().intValue())))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().toString())))
                .andExpect(jsonPath("$.item.id", equalTo(bookingResponse.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", equalTo(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.item.description", equalTo(bookingResponse.getItem().getDescription())))
                .andExpect(jsonPath("$.booker.name", equalTo(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", equalTo(bookingResponse.getBooker().getEmail())));
    }

    @Test
    void shouldRetruenBooking() throws Exception {
        Item item = Item.builder()
                .id(1L)
                .name("itemName")
                .description("itemDesc")
                .available(true)
                .build();

        User booker = User.builder()
                .id(2L)
                .name("userName")
                .email("email@user.com")
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .id(1L)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(1L))
                .end(LocalDateTime.now().plusDays(1L))
                .status(BookingStatus.APPROVED)
                .booker(booker)
                .build();

        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingResponse);

        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(bookingResponse.getId().intValue())))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().toString())))
                .andExpect(jsonPath("$.item.id", equalTo(bookingResponse.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", equalTo(bookingResponse.getItem().getName())))
                .andExpect(jsonPath("$.item.description", equalTo(bookingResponse.getItem().getDescription())))
                .andExpect(jsonPath("$.booker.name", equalTo(bookingResponse.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", equalTo(bookingResponse.getBooker().getEmail())));
    }
}
