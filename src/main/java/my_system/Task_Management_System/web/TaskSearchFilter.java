package my_system.Task_Management_System.web;

import my_system.Task_Management_System.dto.TaskPriority;
import my_system.Task_Management_System.dto.TaskStatus;

public record TaskSearchFilter(
        Long creatorId,
        Long assignedUserId,
        TaskStatus status,
        TaskPriority priority,
        Integer pageSize,
        Integer pageNumber
) {
}
