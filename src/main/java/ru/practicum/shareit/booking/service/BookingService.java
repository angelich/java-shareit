package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingFilterState;

import java.util.List;

public interface BookingService {
    BookingResponse bookItem(Long userId, BookingDto bookingDto);

    BookingResponse approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingResponse getBooking(Long userId, Long bookingId);

    List<BookingResponse> getUserBookings(Long userId, BookingFilterState state);

    List<BookingResponse> getOwnerBookings(Long userId, BookingFilterState state);
}
