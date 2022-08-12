package ru.practicum.shareit.booking;


/**
 * Статусы бронирования
 */
public enum BookingStatus {

    /**
     * Бронирование, ожидающее подтверждения
     */
    WAITING_FOR_APPROVE,

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
