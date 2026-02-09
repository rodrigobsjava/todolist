package com.example.todolist.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.todolist.entity.Task;
import com.example.todolist.entity.User;
import com.example.todolist.service.TaskService;
import com.example.todolist.service.UserService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@GetMapping
	public String listTasks(Model model, @AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(defaultValue = "0") int page) {
		User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
		var taskPage = taskService.getTasksPaginated(user, page);

//        model.addAttribute("tasks", taskService.getTasks(user));
		model.addAttribute("tasks", taskPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", taskPage.getTotalPages());
		return "dashboard";
	}

	@GetMapping("/new")
	public String showForm(Model model) {
		model.addAttribute("task", new Task());
		return "form-task";
	}

	@PostMapping("/save")
	public String saveTask(@ModelAttribute("task") Task task, @AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes) {
		User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
		task.setUser(user);
		taskService.save(task);

		redirectAttributes.addFlashAttribute("successMessage", "Tarefa salva com sucesso.");

		return "redirect:/tasks";
	}

	@PostMapping("/{id}/toggle-status")
	public String toggleTaskStatus(@PathVariable UUID id, @RequestParam(defaultValue = "0") int page,
			RedirectAttributes redirectAttributes) {
		taskService.toggleStatus(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "Status da tarefa atualizado.");
		
		return "redirect:/tasks?page=" + page;
	}

	@GetMapping("/edit/{id}")
	public String editTask(@PathVariable UUID id, Model model) {
		Task task = taskService.getById(id);
		model.addAttribute("task", task);
		return "form-task";
	}

	@PostMapping("/delete/{id}")
	public String deleteTask(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
		taskService.delete(id);

		redirectAttributes.addFlashAttribute("successMessage", "Tarefa excluída com sucesso.");

		return "redirect:/tasks";
	}
}