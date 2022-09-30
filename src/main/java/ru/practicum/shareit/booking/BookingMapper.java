package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public final class BookingMapper {
    private BookingMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .startDate(booking.getStart())
                .endDate(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .item(bookingDto.getItem())
                .booker(bookingDto.getBooker())
                .start(bookingDto.getStartDate())
                .end(bookingDto.getEndDate())
                .status(bookingDto.getStatus())
                .build();
    }
}
