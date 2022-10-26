package ru.practicum.shareit.booking.model;


/**
 * Статусы бронирования
 */
public enum BookingStatus {

    /**
     * Бронирование, ожидающее подтверждения
     */
    WAITING,

    /**
     * Подтвержденное бронирование
     */
    APPROVED,

    /**
     * Бронирование отклонено владельцем
     */
    REJECTED,

    /**
     * Бронирование отменено создателем
     */
    CANCELLED
}
