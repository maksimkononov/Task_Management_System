package my_system.Task_Management_System.Entity;

import jakarta.persistence.*;
import my_system.Task_Management_System.TaskPriority;
import my_system.Task_Management_System.TaskStatus;

import java.time.LocalDateTime;

@Table(name="tasks")
@Entity
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="creatorId", nullable = false)
    private Long creatorId;

    @Column(name="assignedUserId")
    private Long assignedUserId;

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name="createDateTime", nullable = false)
    private LocalDateTime createDateTime;

    @Column(name="deadlineDate", nullable = false)
    private LocalDateTime deadlineDate;

    @Column(name="priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    public TaskEntity() {
    }

    public TaskEntity(
            Long id,
            Long creatorId,
            Long assignedUserId,
            TaskStatus status,
            LocalDateTime createDateTime,
            LocalDateTime deadlineDate,
            TaskPriority priority
    ) {
        this.id = id;
        this.creatorId = creatorId;
        this.assignedUserId = assignedUserId;
        this.status = status;
        this.createDateTime = createDateTime;
        this.deadlineDate = deadlineDate;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDateTime deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format(
                "{id = %d, creatorId = %d, assignedUserId = %d, status = %s, createDateTime = %s, deadlineDate = %s, priority = %s}",
                id, creatorId, assignedUserId, status, createDateTime, deadlineDate, priority
        );
    }
}
