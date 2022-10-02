package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public final class BookingMapper {
    private BookingMapper() {
    }

    public static BookingResponse toBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User user) {
        return Booking.builder()
                .item(item)
                .booker(user)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }
}
