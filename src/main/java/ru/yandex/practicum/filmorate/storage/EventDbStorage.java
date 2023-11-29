package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * DAO для {@link Event}.
 */
@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Event create(Event item) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "insert into events (created_at, user_id, type, operation, entity_id) " +
                "values (CURRENT_TIMESTAMP(), ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setLong(1, item.getUserId());
            ps.setInt(2, item.getType().getCode());
            ps.setInt(3, item.getOperation().getCode());
            ps.setLong(4, item.getEntityId());
            return ps;
        }, keyHolder);

        item.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return item;
    }

    @Override
    public List<Event> getEventsByUserid(Long userId, int count) {
        String query = "SELECT id, created_at, user_id, type, operation, entity_id\n" +
                "FROM events\n" +
                "WHERE user_id = ?\n" +
                "ORDER BY created_at ASC \n" +
                "LIMIT ?";

        return jdbcTemplate.query(query,
                EventDbStorage::toEvent,
                userId, count);
    }

    @Override
    public Event getById(Long id) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public List<Event> getAll() {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public Event update(Event item) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    @Override
    public boolean exists(Long id) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    private static Event toEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .id(rs.getLong("id"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .userId(rs.getLong("user_id"))
                .type(EventType.valueOf(rs.getInt("type")))
                .operation(Operation.valueOf(rs.getInt("operation")))
                .entityId(rs.getLong("entity_id"))
                .build();
    }
}
