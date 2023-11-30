package ru.yandex.practicum.filmorate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Событие пользователя")
public class EventResponse {
    @Schema(description = "Идентификатор", example = "1")
    private Long eventId;

    @Schema(description = "Метка времени эпохи Unix", example = "1")
    private Long timestamp;

    @Schema(description = "Идентификатор пользователя (автор действия)", example = "1")
    private Long userId;

    @Schema(description = "Тип события", example = "1")
    private String eventType;

    @Schema(description = "Тип операции", example = "1")
    private String operation;

    @Schema(description = "Идентификатор объекта (в отношении которого произошло действие)", example = "3")
    private Long entityId;
}
