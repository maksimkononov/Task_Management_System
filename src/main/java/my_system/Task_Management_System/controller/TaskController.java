package my_system.Task_Management_System.controller;

import jakarta.persistence.EntityNotFoundException;
import my_system.Task_Management_System.dto.Task;
import my_system.Task_Management_System.service.TaskService;
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

        try{
            Task task = taskService.findTaskById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(task);
        } catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        log.info("Called method getAllTasks");
        try{
            List<Task> allTasks = taskService.findAllTasks();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(allTasks);
        } catch (EntityNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Task taskToCreate
    ){
        log.info("Called method createTask: task = {}", taskToCreate);

        try{
            Task newTask = taskService.createTask(taskToCreate);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newTask);
        } catch (IllegalArgumentException | IllegalStateException e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("id") Long id,
            @RequestBody Task taskToUpdate
    ){
        log.info("Called method updateTaskById: id = {}", id);
        try{
            Task updatedTask = taskService.updateTask(id, taskToUpdate);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(updatedTask);
        } catch(IllegalStateException e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id
    ){
        log.info("Called method deleteTaskById: id = {}", id);
        try{
            taskService.cancelTask(id);
            return ResponseEntity
                    .ok()
                    .build();
        } catch(NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> inProgressTask(
            @PathVariable("id") Long id
    ){
        log.info("Called method inProgressTask: id = {}", id);

        try {
            return ResponseEntity
                    .ok(taskService.taskToStart(id));
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }
}
