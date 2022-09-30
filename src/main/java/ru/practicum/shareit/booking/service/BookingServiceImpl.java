package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
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
import static ru.practicum.shareit.booking.BookingMapper.toBookingDto;
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
    public BookingDto bookItem(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("User not exist"));
        Item item = itemRepository.findById(bookingDto.getItem().getId()).orElseThrow(
                () -> new NoSuchElementException("Item not exist"));

        //Старт бронирования не может быть позже конца бронирования и не позже сегодняшнего дня
        if (bookingDto.getStartDate().isAfter(bookingDto.getEndDate()) || bookingDto.getStartDate().isBefore(now().minusDays(1L))) {
            throw new IllegalArgumentException("Invalid booking time");
        }

        Booking booking = toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return toBookingDto(savedBooking);
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        userService.checkUserExist(userId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NoSuchElementException("Booking not exist"));

        if (booking.getItem().getOwner().getId().compareTo(userId) != 0) {
            throw new NoSuchElementException("Booking not exist");
        }

        BookingStatus status = approved ? APPROVED : REJECTED;
        booking.setStatus(status);

        Booking savedBooking = bookingRepository.save(booking);
        return toBookingDto(savedBooking);
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        userService.checkUserExist(userId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NoSuchElementException("Booking not exist"));

        //Получить бронирование может либо автор бронирования, либо владелец вещи
        if (booking.getItem().getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            throw new NoSuchElementException("Booking not exist");
        }

        return toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String state) {
        userService.checkUserExist(userId);
        BookingFilterState filterState = BookingFilterState.valueOf(state.toUpperCase());

        switch (filterState) {
            case ALL:
                return bookingRepository.findBookingsByBooker_IdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByBooker_IdAndStartAfterOrderByStartDesc(userId, now().minusDays(1L))
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByBooker_IdAndEndBeforeOrderByStartDesc(userId, now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBooker_IdAndStartAfterOrderByStartDesc(userId, now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByBooker_IdAndStatusIsOrderByStartDesc(userId, REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByBooker_IdAndStatusIsOrderByStartDesc(userId, WAITING)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new IllegalStateException("Illegal query state");
        }
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long userId, String state) {
        userService.checkUserExist(userId);
        BookingFilterState filterState = BookingFilterState.valueOf(state.toUpperCase());

        switch (filterState) {
            case ALL:
                return bookingRepository.findBookingsByItem_Owner_IdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now().minusDays(1L))
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(userId, REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(userId, WAITING)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new IllegalStateException("Illegal query state");
        }
    }
}
