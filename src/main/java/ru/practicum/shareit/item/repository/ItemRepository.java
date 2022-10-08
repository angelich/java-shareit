package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Item> findAllByOwner(User owner, Pageable pageable);

    @Query("SELECT i FROM Item as i " +
            "WHERE i.available = true " +
            "AND (LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%)")
    Page<Item> findAllByText(String text, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);
}
