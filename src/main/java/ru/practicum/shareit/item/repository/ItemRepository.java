package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Хранилище вещей
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User owner);

    @Query("SELECT i FROM Item as i " +
            "WHERE i.available = true " +
            "AND (LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%)")
    List<Item> findAllByText(String text);

    List<Item> findAllByRequestId(Long requestId);
}
