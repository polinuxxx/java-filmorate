package ru.yandex.practicum.filmorate.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Operation {
    ADD(0),
    UPDATE(1),
    REMOVE(2);

    private final int code;

    public static Operation valueOf(int code) {
        return Arrays.stream(Operation.values())
                .filter(v -> v.getCode() == code)
                .findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(Operation.class, Integer.toString(code)));
    }
}
