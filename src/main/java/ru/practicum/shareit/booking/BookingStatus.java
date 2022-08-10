package ru.practicum.shareit.booking;


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
