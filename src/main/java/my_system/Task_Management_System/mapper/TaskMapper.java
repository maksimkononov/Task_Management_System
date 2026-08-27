package my_system.Task_Management_System.mapper;

import my_system.Task_Management_System.Entity.TaskEntity;
import my_system.Task_Management_System.dto.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toDomain(TaskEntity taskEntity){
        return new Task(
                taskEntity.getId(),
                taskEntity.getCreatorId(),
                taskEntity.getAssignedUserId(),
                taskEntity.getStatus(),
                taskEntity.getCreateDateTime(),
                taskEntity.getDeadlineDate(),
                taskEntity.getPriority()
        );
    }

    public TaskEntity toEntity(Task task){
        return new TaskEntity(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.status(),
                task.createDateTime(),
                task.deadlineDate(),
                task.priority()
        );
    }

    public void updateEntityFromDomain(Task dto, TaskEntity entity){
        if(dto.assignedUserId() != null){
            entity.setAssignedUserId(dto.assignedUserId());
        }
        if(dto.deadlineDate() != null){
            entity.setDeadlineDate(dto.deadlineDate());
        }
        if(dto.priority() != null){
            entity.setPriority(dto.priority());
        }
    }
}
