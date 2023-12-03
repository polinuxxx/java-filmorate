package ru.yandex.practicum.filmorate.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EventType {
    LIKE(0),
    FRIEND(1),
    REVIEW(2);

    private final int code;

    public static EventType valueOf(int code) {
        return Arrays.stream(EventType.values())
                .filter(v -> v.getCode() == code)
                .findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(EventType.class, Integer.toString(code)));
    }
}
