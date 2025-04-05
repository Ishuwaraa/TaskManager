package com.treinetic.TaskManager.service;

import com.treinetic.TaskManager.DTO.request.TaskDTO;
import com.treinetic.TaskManager.model.MyUser;
import com.treinetic.TaskManager.model.Task;
import com.treinetic.TaskManager.repository.TaskRepository;
import com.treinetic.TaskManager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public MyUser createTestUser(MyUser user) {
        MyUser newUser = new MyUser();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(user.getPassword());
        return userRepository.save(newUser);
    }

    public List<Task> getAllTasksCreatedByUser() {
        return taskRepository.findByUserEmail("ishuwara@gmail.com");
    }

    public Task getTaskByIdCreatedByUser(Long id) {
        return taskRepository.findByIdAndUserEmail(id, "ishuwara@gmail.com")
                .orElse(null);
    }

    public Task createTask(TaskDTO dto) {
        MyUser user = userRepository.findByEmail("ishuwara@gmail.com")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setUser(user);

        return taskRepository.save(task);
    }

    public Task updateTask(int id, TaskDTO dto) {
        Task task = taskRepository.findByIdAndUserEmail((long) id, "wedage@gmail.com")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());

        return taskRepository.save(task);
    }

    public void deleteTask(int id) {
        Task task = taskRepository.findByIdAndUserEmail((long) id, "wedage@gmail.com")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No task found"));
        taskRepository.deleteById((task.getId()));
    }
}
