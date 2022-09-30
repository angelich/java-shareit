package ru.practicum.shareit.booking;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBooker_IdOrderByStartDesc(Long userId);

    List<Booking> findBookingsByBooker_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByBooker_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByBooker_IdAndStatusIsOrderByStartDesc(Long userId, BookingStatus status);

    List<Booking> findBookingsByItem_Owner_IdOrderByStartDesc(Long userId);

    List<Booking> findBookingsByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByItem_Owner_IdAndStatusIsOrderByStartDesc(Long userId, BookingStatus status);

    @Query("SELECT b.start FROM Booking AS b " +
            "WHERE b.start > current_time " +
            "AND b.item.id = :itemId " +
            "ORDER BY b.start ASC")
    LocalDateTime findNextBookingDateByItemId(Long itemId);

    @Query("SELECT b.start FROM Booking AS b " +
            "WHERE b.start > current_time " +
            "AND b.item.id = :itemId " +
            "ORDER BY b.start DESC")
    LocalDateTime findLastBookingDateByItemId(Long itemId);

    boolean existsByBooker_IdAndItem_IdAndEndBefore(Long userId, Long itemId, LocalDateTime dateTime);
}
