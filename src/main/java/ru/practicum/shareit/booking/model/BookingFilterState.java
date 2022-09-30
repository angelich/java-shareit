package ru.practicum.shareit.booking.model;


/**
 * Статусы жизненного цикла бронирования для фильтра
 */
public enum BookingFilterState {

    /**
     * Все бронирования
     */
    ALL,

    /**
     * Текущее бронирование
     */
    CURRENT,

    /**
     * Бронирование в прошлом
     */
    PAST,

    /**
     * Будущее бронирование
     */
    FUTURE,

    /**
     * Ожидающее подтверждения
     */
    WAITING,

    /**
     * Отклоненное владельцем
     */
    REJECTED,

    /**
     * Неподдерживаемый статус
     */
    UNSUPPORTED_STATUS
}
