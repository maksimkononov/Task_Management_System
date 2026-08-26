package my_system.Task_Management_System;

import java.time.LocalDateTime;

public record Task(
        Long id,
        Long creatorId,
        Long assignedUserId,
        TaskStatus status,
        LocalDateTime createDateTime,
        LocalDateTime deadlineDate,
        TaskPriority priority
) {
}
