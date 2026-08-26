package my_system.Task_Management_System.services;

import my_system.Task_Management_System.Task;
import my_system.Task_Management_System.TaskPriority;
import my_system.Task_Management_System.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private AtomicLong idCounter;
    private HashMap<Long, Task> map;

    public TaskService(){
        idCounter = new AtomicLong(0);
        map = new HashMap<>(Map.of(
                idCounter.incrementAndGet(), new Task(
                        idCounter.get(),
                        1L,
                        1L,
                        TaskStatus.CREATED,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        TaskPriority.LOW
                ),
                idCounter.incrementAndGet(), new Task(
                        idCounter.get(),
                        2L,
                        2L,
                        TaskStatus.CREATED,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        TaskPriority.LOW
                ),
                idCounter.incrementAndGet(), new Task(
                        idCounter.get(),
                        3L,
                        3L,
                        TaskStatus.CREATED,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        TaskPriority.LOW
                )
        ));
    }

    public Task findTaskById(Long id) {
        log.info("Called method findTaskById: id = {}", id);
        if(!map.containsKey(id)){
            throw new NoSuchElementException("Not found task with id = " + id);
        }

        return map.get(id);
    }


    public List<Task> findAllTasks() {
        log.info("Called method findAllTasks");
        List<Task> allTasks = map.values().stream().toList();

        if(allTasks.isEmpty()){
            throw new NoSuchElementException("No tasks were found");
        }

        return allTasks;
    }

    public Task createTask(Task taskToCreate) {
        if(taskToCreate.id() != null){
            throw new IllegalArgumentException("Id should be null");
        }

        if(taskToCreate.status() != null){
            throw new IllegalStateException("Status should be null");
        }

        Task newTask = new Task(
                idCounter.incrementAndGet(),
                taskToCreate.creatorId(),
                taskToCreate.assignedUserId(),
                TaskStatus.CREATED,
                taskToCreate.createDateTime(),
                taskToCreate.deadlineDate(),
                taskToCreate.priority()
        );
        map.put(newTask.id(), newTask);

        return newTask;
    }

    public Task updateTask(Long id, Task taskToUpdate) {
        if(!map.containsKey(id)){
            throw new NoSuchElementException("Not found task with id = " + id);
        }

        Task oldTask = map.get(id);

        if(!oldTask.status().equals(TaskStatus.CREATED)){
            throw new IllegalStateException("Task cannot update because status is DONE");
        }

        Task updatedTask = new Task(
                oldTask.id(),
                taskToUpdate.creatorId(),
                taskToUpdate.assignedUserId(),
                TaskStatus.CREATED,
                taskToUpdate.createDateTime(),
                taskToUpdate.deadlineDate(),
                taskToUpdate.priority()
        );
        map.put(updatedTask.id(), updatedTask);

        return updatedTask;


    }

    public void cancelTask(Long id) {
        if(!map.containsKey(id)){
            throw new NoSuchElementException("Not found task with id = " + id);
        }

        Task deletedTask = map.get(id);

        if(!deletedTask.status().equals(TaskStatus.CREATED)){
            throw new IllegalStateException("Status should be CREATED");
        }
        map.remove(id);

        log.info("Successfully cancelled task: id = " + id);
    }
}
