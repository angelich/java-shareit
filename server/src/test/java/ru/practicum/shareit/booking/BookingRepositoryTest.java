package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookingRepositoryTest {
    private final PageRequest pageRequest = PageRequest.of(0, 10);
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private User owner;
    private Item item1;
    private Booking booking1;
    private User booker;

    @BeforeEach
    void beforeEach() {
        owner = userRepository.save(User.builder()
                .name("name")
                .email("email@email.ru")
                .build());

        booker = userRepository.save(User.builder()
                .name("name booker")
                .email("emailBooker@email.ru")
                .build());
        item1 = itemRepository.save(Item.builder()
                .name("itemName")
                .description("descItem")
                .available(true)
                .owner(owner)
                .build());

        booking1 = bookingRepository.save(Booking.builder()
                .item(item1)
                .start(LocalDateTime.now().plusMinutes(1L))
                .end(LocalDateTime.now().plusDays(1L))
                .status(BookingStatus.WAITING)
                .booker(booker)
                .build());
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllUserBookings() {
        Page<Booking> bookings = bookingRepository.findBookingsByBooker_IdAndStatusIsOrderByStartDesc(booker.getId(), BookingStatus.WAITING, pageRequest);
        Booking foundBooking = bookings.stream().findFirst().get();

        assertNotNull(foundBooking.getId());
        assertEquals(booker.getId(), foundBooking.getBooker().getId());
        assertEquals(item1.getId(), foundBooking.getItem().getId());
        assertEquals(BookingStatus.WAITING, foundBooking.getStatus());
        assertNotNull(foundBooking.getStart());
        assertNotNull(foundBooking.getEnd());
    }

}
