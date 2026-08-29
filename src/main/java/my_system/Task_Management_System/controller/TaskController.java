package my_system.Task_Management_System.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import my_system.Task_Management_System.dto.Task;
import my_system.Task_Management_System.dto.TaskPriority;
import my_system.Task_Management_System.dto.TaskStatus;
import my_system.Task_Management_System.service.TaskService;
import my_system.Task_Management_System.web.TaskSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("id") Long id
    ){
        log.info("Called method getTaskById: id = {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.findTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(name = "creatorId", required = false) Long creatorId,
            @RequestParam(name = "assignedUserId", required = false) Long assignedUserId,
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) TaskPriority priority,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ){
        log.info("Called method getAllTasks");

        TaskSearchFilter filter = new TaskSearchFilter(
                creatorId,
                assignedUserId,
                status,
                priority,
                pageSize,
                pageNumber
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.findAllTasksByFilter(filter));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid Task taskToCreate
    ){
        log.info("Called method createTask: task = {}", taskToCreate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(taskToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("id") Long id,
            @RequestBody @Valid Task taskToUpdate
    ){
        log.info("Called method updateTaskById: id = {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.updateTask(id, taskToUpdate));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id
    ){
        log.info("Called method deleteTaskById: id = {}", id);

        taskService.cancelTask(id);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> inProgressTask(
            @PathVariable("id") Long id
    ){
        log.info("Called method inProgressTask: id = {}", id);

        return ResponseEntity
                .ok(taskService.taskToStart(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(
            @PathVariable("id") Long id
    ){
        log.info("Called method completeTask: id = {}", id);

        return ResponseEntity
                .ok(taskService.completeTask(id));
    }
}
