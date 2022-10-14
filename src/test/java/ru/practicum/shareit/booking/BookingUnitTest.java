package ru.practicum.shareit.booking;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class BookingUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    @BeforeEach
    void beforeEach() {
        bookingService = new BookingServiceImpl(
                bookingRepository,
                userRepository,
                userService,
                itemRepository);
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldBookItem() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto(1L, now().plusMinutes(1L), now().plusDays(1L), item.getId(), booker.getId());

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));

        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        var savedBooking = bookingService.bookItem(booker.getId(), bookingDto);

        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldApproveBooking() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "booker", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);

        Mockito
                .when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));

        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        var approvedBooking = bookingService.approveBooking(owner.getId(), booking.getId(), true);

        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
        assertEquals(item.getId(), approvedBooking.getItem().getId());
        assertEquals(booker.getId(), approvedBooking.getBooker().getId());
        assertNotNull(approvedBooking.getStart());
        assertNotNull(approvedBooking.getEnd());
    }

    @Test
    void shouldReturnBooking() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);

        Mockito
                .when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));

        var savedBooking = bookingService.getBooking(booker.getId(), booking.getId());

        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunAllUserBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByBooker_IdOrderByStartDesc(any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getUserBookings(booker.getId(), "ALL", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunCurrentUserBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByBooker_IdAndStartBeforeAndEndAfter(any(), any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getUserBookings(booker.getId(), "CURRENT", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunPastUserBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByBooker_IdAndEndBeforeOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getUserBookings(booker.getId(), "PAST", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunFutureUserBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByBooker_IdAndStartAfterOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getUserBookings(booker.getId(), "FUTURE", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunRejectedUserBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByBooker_IdAndStatusIsOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getUserBookings(booker.getId(), "REJECTED", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunWaitingUserBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByBooker_IdAndStatusIsOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getUserBookings(booker.getId(), "WAITING", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunAllOwnerBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByItem_Owner_IdOrderByStartDesc(any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getOwnerBookings(owner.getId(), "ALL", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunCurrentOwnerBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByItem_Owner_IdAndStartBeforeAndEndAfter(any(), any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getOwnerBookings(owner.getId(), "CURRENT", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunPastOwnerBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByItem_Owner_IdAndEndBeforeOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getOwnerBookings(owner.getId(), "PAST", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunFutureOwnerBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByItem_Owner_IdAndStartAfterOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getOwnerBookings(owner.getId(), "FUTURE", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunRejectedOwnerBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getOwnerBookings(owner.getId(), "REJECTED", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }

    @Test
    void shouldRetunWaitingOwnerBookings() {
        User owner = new User(1L, "owner", "owner@email.com");
        User booker = new User(2L, "userName", "booker@email.com");
        Item item = new Item(1L, "itemName", "itemDesc", true, owner, null);
        Booking booking = new Booking(1L, item, booker, now().plusMinutes(1L), now().plusDays(1L), BookingStatus.WAITING);
        Page<Booking> page = new PageImpl(List.of(booking));

        Mockito
                .when(bookingRepository.findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(any(), any(), any()))
                .thenReturn(page);

        var savedBookings = bookingService.getOwnerBookings(owner.getId(), "WAITING", 0, 10);
        var savedBooking = savedBookings.get(0);

        assertEquals(1, savedBookings.size());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertNotNull(savedBooking.getStart());
        assertNotNull(savedBooking.getEnd());
    }
}
