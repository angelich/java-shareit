package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

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
}
