package ru.gromdv.taskService.controller;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gromdv.taskService.dto.DtoMapper;
import ru.gromdv.taskService.dto.TaskDto;
import ru.gromdv.taskService.model.Task;
import ru.gromdv.taskService.model.TaskStatus;
import ru.gromdv.taskService.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
@Log
@RequestMapping("/tasks/api")
public class RestController {

    private final TaskService servise;
    private final DtoMapper dtoMapper;

    @GetMapping("/list")
    public ResponseEntity<List<TaskDto>> getTasksList() {
        List<TaskDto> tasks = servise.getTasksList()
                .stream().map(dtoMapper::toDto).toList();
//        log.log(Level.INFO, String.format("Tasks LIST: %s", tasks));
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/list-by-dev/{id}")
    public ResponseEntity<List<TaskDto>> getDeveloperTasksList(@PathVariable Long id) {
        List<TaskDto> tasks = servise.getDeveloperTasksList(id)
                .stream().map(dtoMapper::toDto).toList();
//        log.log(Level.INFO, String.format("Tasks LIST: %s", tasks));
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/list/status/{st}")
    public ResponseEntity<List<TaskDto>> getTasksList(@PathVariable String st) {
        TaskStatus status = switch (st) {
            case "new" -> TaskStatus.NEW_TASK;
            case "progress" -> TaskStatus.IN_PROGRESS;
            case "completed" -> TaskStatus.COMPLETED;
            case "paused" -> TaskStatus.PAUSED;
            case "urgent" -> TaskStatus.URGENT;
            default -> TaskStatus.CANCELED;
        };
        List<TaskDto> tasks = servise.getTasksList()
                .stream()
                .filter(x -> x.getStatus().equals(status))
                .map(dtoMapper::toDto)
                .toList();
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/list/status-dev/{status}/{devId}")
    public ResponseEntity<List<TaskDto>> getTasksList(@PathVariable String status, @PathVariable Long devId) {
        TaskStatus statusU = switch (status) {
            case "new" -> TaskStatus.NEW_TASK;
            case "progress" -> TaskStatus.IN_PROGRESS;
            case "completed" -> TaskStatus.COMPLETED;
            case "paused" -> TaskStatus.PAUSED;
            case "urgent" -> TaskStatus.URGENT;
            default -> TaskStatus.CANCELED;
        };
        List<TaskDto> tasks = servise.getTasksList()
                .stream()
                .filter(x -> x.getDeveloperId().equals(devId))
                .filter(x -> x.getStatus().equals(statusU))
                .map(dtoMapper::toDto)
                .toList();
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewTask(@RequestBody Task t) {
        t.setStatus(TaskStatus.NEW_TASK);
        t.setDateCreate(LocalDateTime.now());
        servise.addNewTask(t);
        return ResponseEntity.ok().body(t);
    }
    @PutMapping("/update")
    public void putTask(@RequestBody Task task) {
        Task oldTask = servise.getTaskById(task.getId());
        if(oldTask.getStatus() != TaskStatus.COMPLETED && task.getStatus() == TaskStatus.COMPLETED)
            task.setDateComplete(LocalDateTime.now());
        servise.saveTask(task);
    }
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTasksList(@PathVariable Long id) {
        TaskDto task = new DtoMapper().toDto(servise.getTaskById(id));
        log.log(Level.INFO, String.format("Task: %s", task));
        if(task != null)
            return ResponseEntity.ok().body(task);
        else
            return ResponseEntity.notFound().build();
    }
}
