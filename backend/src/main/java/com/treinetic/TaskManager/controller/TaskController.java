package com.treinetic.TaskManager.controller;

import com.treinetic.TaskManager.DTO.request.TaskDTO;
import com.treinetic.TaskManager.model.MyUser;
import com.treinetic.TaskManager.model.Task;
import com.treinetic.TaskManager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasksCreatedByUser(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        Task res = taskService.getTaskByIdCreatedByUser((long) id);

        if (res == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO dto) {
        return new ResponseEntity<>(taskService.createTask(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody TaskDTO dto) {
        return new ResponseEntity<>(taskService.updateTask(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/test/user")
    public ResponseEntity<?> createTestUser(@RequestBody MyUser myUser) {
        return new ResponseEntity<>(taskService.createTestUser(myUser), HttpStatus.CREATED);
    }
}
