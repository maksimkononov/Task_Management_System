package my_system.Task_Management_System.repository;

import my_system.Task_Management_System.Entity.TaskEntity;
import my_system.Task_Management_System.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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

}
