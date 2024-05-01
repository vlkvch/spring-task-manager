package org.example.dao;

import java.time.LocalDate;
import java.util.List;

import org.example.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class TaskDAO {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public List<Task> getAll() {
        return jdbcTemplate.query("SELECT * FROM tasks", new BeanPropertyRowMapper<>(Task.class));
    }

    public Task getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM tasks WHERE id = ?", new BeanPropertyRowMapper<>(Task.class), id);
    }

    public List<Task> getByDueDate(LocalDate date) {
        return jdbcTemplate.query("SELECT * FROM tasks WHERE duedate = ?", new BeanPropertyRowMapper<>(Task.class), date);
    }

    public void add(Task task) {
        jdbcTemplate.update(
            "INSERT INTO tasks(title, description, duedate) VALUES(?, ?, ?)",
            task.getTitle(),
            task.getDescription(),
            task.getDueDate()
        );
    }

    public void update(int id, Task updatedTask) {
        jdbcTemplate.update(
            "UPDATE tasks SET title = ?, description = ?, dueDate = ? WHERE id = ?",
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getDueDate(),
            id
        );
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }

    public List<Task> getTodaysTasks() {
        return jdbcTemplate.query("SELECT * FROM tasks WHERE duedate = ?", new BeanPropertyRowMapper<>(Task.class), LocalDate.now());
    }

}
