package my_system.Task_Management_System.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record Task(
        @Null
        Long id,

        @NotNull
        Long creatorId,

        Long assignedUserId,

        TaskStatus status,

        @NotNull
        LocalDateTime createDateTime,

        @NotNull
        @FutureOrPresent
        LocalDateTime deadlineDate,

        @NotNull
        TaskPriority priority,

        @Null
        LocalDateTime doneDateTime
) {
}
