package ru.practicum.shareit.requests;


import ru.practicum.shareit.requests.dto.ItemRequestDto;

public final class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequestDto toItemDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreatedDateTime())
                .description(itemRequest.getDescription())
                .build();
    }
}
