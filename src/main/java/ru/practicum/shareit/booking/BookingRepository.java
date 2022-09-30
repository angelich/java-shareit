package ru.practicum.shareit.booking;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBooker_IdOrderByStartDesc(Long userId);

    List<Booking> findBookingsByBooker_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByBooker_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByBooker_IdAndStatusIsOrderByStartDesc(Long userId, BookingStatus status);

    List<Booking> findBookingsByItem_Owner_IdOrderByStartDesc(Long userId);

    List<Booking> findBookingsByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByItem_Owner_IdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBooker_IdAndStartBeforeAndEndAfter(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(Long userId, BookingStatus status);

    Booking findTopByItem_IdAndItem_OwnerAndStartAfterOrderByStartAsc(Long itemId, User user, LocalDateTime start);

    Booking findTopByItem_IdAndItem_OwnerAndEndBeforeOrderByEndDesc(Long itemId, User user, LocalDateTime end);

    boolean existsByBooker_IdAndItem_IdAndEndBefore(Long userId, Long itemId, LocalDateTime dateTime);
}
