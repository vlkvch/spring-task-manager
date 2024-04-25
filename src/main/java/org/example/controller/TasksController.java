package org.example.controller;

import java.time.LocalDate;

import org.example.dao.TaskDAO;
import org.example.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TasksController {
    
    @Autowired
    private final TaskDAO taskDAO;

    /**
     * Returns a view with all the tasks.
     */
    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("tasks", taskDAO.getAll());
        return "tasks";
    }

    /**
     * Returns a view for a task based on its ID.
     */
    @GetMapping("/{id:\\d+}")
    public String getById(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", taskDAO.getById(id));
        return "task";
    }

    /**
     * Gets today's tasks.
     */
    @GetMapping("/today")
    public String today(Model model) {
        model.addAttribute("todaysTasks", taskDAO.getTodaysTasks());
        return "today";
    }

    /**
     * Gets tasks by their due date.
     *
     * The format is "YYYY-MM-DD".
     */
    @GetMapping("/{date:\\d{4}-\\d{2}-\\d{2}}")
    public String getByDate(@PathVariable("date") LocalDate date, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("tasksByDate", taskDAO.getByDueDate(date));
        return "date";
    }

    /**
     * Adds a task.
     */
    @PostMapping
    public String add(@ModelAttribute("task") Task task) {
        taskDAO.add(task);
        return "redirect:/tasks";
    }

    /**
     * Returns a view for creation of a new task.
     */
    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") Task task) {
        return "new";
    }

    /**
     * Updates a task based on its ID.
     */
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("Task") Task task) {
        taskDAO.update(id, task);
        return "redirect:/tasks";
    }


    /**
     * Returns a view for editing a task.
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", taskDAO.getById(id));
        return "edit";
    }

    /**
     * Deletes a task based on its ID.
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        taskDAO.delete(id);
        return "redirect:/tasks";
    }

}
