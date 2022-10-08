package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingFilterState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static ru.practicum.shareit.booking.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.BookingMapper.toBookingResponse;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.REJECTED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, UserService userService, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingResponse bookItem(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("User not exist"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(
                () -> new NoSuchElementException("Item not exist"));

        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Item not available");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new NoSuchElementException("Owner can't book his item");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new IllegalArgumentException("Invalid booking time");
        }

        Booking booking = toBooking(bookingDto, item, user);
        booking.setStatus(WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return toBookingResponse(savedBooking);
    }

    @Override
    public BookingResponse approveBooking(Long userId, Long bookingId, Boolean approved) {
        userService.checkUserExist(userId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NoSuchElementException("Booking not exist"));

        if (booking.getItem().getOwner().getId().compareTo(userId) != 0) {
            throw new NoSuchElementException("Booking not allowed");
        }

        if (approved && APPROVED == booking.getStatus()) {
            throw new IllegalArgumentException("Booking can't be approved in current status");
        }

        BookingStatus status = approved ? APPROVED : REJECTED;
        booking.setStatus(status);

        Booking savedBooking = bookingRepository.save(booking);
        return toBookingResponse(savedBooking);
    }

    @Override
    public BookingResponse getBooking(Long userId, Long bookingId) {
        userService.checkUserExist(userId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NoSuchElementException("Booking not exist"));

        //Получить бронирование может либо автор бронирования, либо владелец вещи
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new NoSuchElementException("Access denied");
        }

        return toBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId, String state, Integer from, Integer size) {
        userService.checkUserExist(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        BookingFilterState filterState = BookingFilterState.valueOf(state.toUpperCase());

        switch (filterState) {
            case ALL:
                return bookingRepository.findBookingsByBooker_IdOrderByStartDesc(userId, pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByBooker_IdAndStartBeforeAndEndAfter(userId, now(), now(), pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByBooker_IdAndEndBeforeOrderByStartDesc(userId, now(), pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBooker_IdAndStartAfterOrderByStartDesc(userId, now(), pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByBooker_IdAndStatusIsOrderByStartDesc(userId, REJECTED, pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByBooker_IdAndStatusIsOrderByStartDesc(userId, WAITING, pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Unknown state: " + filterState);
        }
    }

    @Override
    public List<BookingResponse> getOwnerBookings(Long userId, String state, Integer from, Integer size) {
        userService.checkUserExist(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        BookingFilterState filterState = BookingFilterState.valueOf(state.toUpperCase());

        switch (filterState) {
            case ALL:
                return bookingRepository.findBookingsByItem_Owner_IdOrderByStartDesc(userId, pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByItem_Owner_IdAndStartBeforeAndEndAfter(userId, now(), now(), pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now(), pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now(), pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(userId, REJECTED, pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(userId, WAITING, pageRequest)
                        .stream()
                        .map(BookingMapper::toBookingResponse)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Unknown state: " + filterState);
        }
    }
}
