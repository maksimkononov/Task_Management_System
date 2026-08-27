package my_system.Task_Management_System.dto;

import my_system.Task_Management_System.TaskPriority;
import my_system.Task_Management_System.TaskStatus;

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
