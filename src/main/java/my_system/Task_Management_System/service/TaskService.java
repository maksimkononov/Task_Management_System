package my_system.Task_Management_System.service;

import jakarta.persistence.EntityNotFoundException;
import my_system.Task_Management_System.Entity.TaskEntity;
import my_system.Task_Management_System.dto.Task;
import my_system.Task_Management_System.dto.TaskStatus;
import my_system.Task_Management_System.mapper.TaskMapper;
import my_system.Task_Management_System.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    public Task findTaskById(Long id) {
        log.info("Called method findTaskById: id = {}", id);

        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found task with id = " + id));

        return mapper.toDomain(taskEntity);
    }


    public List<Task> findAllTasks() {
        log.info("Called method findAllTasks");

        List<TaskEntity> allTaskEntity = repository.findAll();

        if(allTaskEntity.isEmpty()){
            throw new EntityNotFoundException("No tasks were found");
        }

        return allTaskEntity.stream().map(mapper::toDomain).toList();
    }


    public Task createTask(Task taskToCreate) {
        if(taskToCreate.id() != null){
            throw new IllegalArgumentException("Id should be null");
        }

        if(taskToCreate.status() != null){
            throw new IllegalStateException("Status should be null");
        }

        TaskEntity newTask = mapper.toEntity(taskToCreate);
        newTask.setStatus(TaskStatus.CREATED);
        TaskEntity savedTask = repository.save(newTask);

        log.info("After save newTask = {}", newTask);

        return mapper.toDomain(savedTask);
    }


    @Transactional
    public Task updateTask(Long id, Task taskToUpdate) {
        TaskEntity oldTaskEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found task with id = " + id));

        if(!oldTaskEntity.getStatus().equals(TaskStatus.CREATED)){
            throw new IllegalStateException("Task cannot update because status is DONE or IN_PROGRESS");
        }

        mapper.updateEntityFromDomain(taskToUpdate, oldTaskEntity);
        return mapper.toDomain(oldTaskEntity);
    }


    @Transactional
    public void cancelTask(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Not found task with id = " + id));

        log.info("taskEntity = {}", taskEntity);

        if(taskEntity.getStatus().equals(TaskStatus.CANCEL)){
            throw new IllegalStateException("The task has already been canceled");
        }
        if(taskEntity.getStatus().equals(TaskStatus.DONE)){
            throw new IllegalStateException("The task has already been done");
        }

        repository.setStatus(id, TaskStatus.CANCEL);

        log.info("Successfully cancelled task: id = {}", id);
    }


    @Transactional
    public Task taskToStart(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found task with id = " + id));

        if(taskEntity.getAssignedUserId() == null){
            throw new IllegalArgumentException("AssignedUserId cannot be null");
        }

        if(!taskEntity.getStatus().equals(TaskStatus.CREATED)){
            throw new IllegalStateException("Status should be CREATED");
        }

        boolean isConflict = isConflictToStartTask(taskEntity.getAssignedUserId());

        if(isConflict){
            throw new IllegalArgumentException("The task cannot be started because of conflict");
        }

        log.info("taskEntity before setStatus = {}", taskEntity);
        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        log.info("taskEntity after setStatus = {}", taskEntity);

        return mapper.toDomain(taskEntity);
    }


    @Transactional
    public Task completeTask(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found task with id = " + id));

        if(taskEntity.getAssignedUserId() == null){
            throw  new IllegalArgumentException("AssignedUserId cannot be null");
        }

        if(taskEntity.getStatus().equals(TaskStatus.DONE)){
            throw new IllegalStateException("The task has already been done");
        }

        if(!taskEntity.getStatus().equals(TaskStatus.IN_PROGRESS)){
            throw new IllegalStateException("Status should be IN_PROGRESS");
        }

        if(taskEntity.getDeadlineDate() == null){
            throw new IllegalArgumentException("DeadlineDate cannot be null");
        }

        taskEntity.setDoneDateTime(LocalDateTime.now());
        taskEntity.setStatus(TaskStatus.DONE);

        return mapper.toDomain(taskEntity);
    }




    private boolean isConflictToStartTask(Long assignedUserId){
        return repository.conflictCountHandleTasks(assignedUserId, TaskStatus.IN_PROGRESS) >= 5;
    }
}
