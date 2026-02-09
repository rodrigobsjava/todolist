package com.example.todolist.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.todolist.entity.Task;
import com.example.todolist.entity.User;
import com.example.todolist.repository.TaskRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;


	public Page<Task> getTasksPaginated(User user, int page) {
		Pageable pageable = PageRequest.of(page, 5);
		return taskRepository.findByUser(user, pageable);
	}

	public void save(Task task) {
		if (task.getId() == null) {
			task.setCreatedAt(LocalDateTime.now());
		} else {
			task.setUpdatedAt(LocalDateTime.now());
		}
		taskRepository.save(task);
	}

	public Task getById(UUID id) {
		return taskRepository.findById(id).orElse(null);
	}

	public void delete(UUID id) {
		taskRepository.deleteById(id);
	}

	public void toggleStatus(UUID id) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task não encontrada"));
		task.setCompleted(!task.isCompleted());
		task.setUpdatedAt(LocalDateTime.now());

		taskRepository.save(task);
	}
}