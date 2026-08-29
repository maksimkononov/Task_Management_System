package my_system.Task_Management_System.repository;

import my_system.Task_Management_System.Entity.TaskEntity;
import my_system.Task_Management_System.dto.TaskPriority;
import my_system.Task_Management_System.dto.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Modifying
    @Query("""
            UPDATE TaskEntity t
            SET t.status = :status
            WHERE t.id = :id
            """)
    void setStatus(
            @Param("id") Long id,
            @Param("status") TaskStatus status
    );

    @Query("""
            SELECT COUNT(t)
            FROM TaskEntity t
            WHERE t.status = :status
            AND t.assignedUserId = :assignedUserId
            """)
    int conflictCountHandleTasks(
            @Param("assignedUserId") Long assignedUserId,
            @Param("status") TaskStatus status
    );

    @Query("""
            SELECT t
            FROM TaskEntity t
            WHERE (t.creatorId = :creatorId OR :creatorId IS NULL)
            AND (t.assignedUserId = :assignedUserId OR :assignedUserId IS NULL)
            AND (t.status = :status OR :status IS NULL)
            AND (t.priority = :priority OR :priority IS NULL)
            """)
    List<TaskEntity> findAllByFilter(
            @Param("creatorId") Long creatorId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            Pageable pageable
    );

}
