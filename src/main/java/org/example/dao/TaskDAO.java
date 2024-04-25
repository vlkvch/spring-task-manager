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
        return jdbcTemplate.query("SELECT * FROM Tasks", new BeanPropertyRowMapper<>(Task.class));
    }

    public Task getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM Tasks WHERE id=?", new BeanPropertyRowMapper<>(Task.class), id);
    }

    public List<Task> getByDueDate(LocalDate date) {
        return jdbcTemplate.query("SELECT * FROM Tasks WHERE duedate=?", new BeanPropertyRowMapper<>(Task.class), date);
    }

    public void add(Task task) {
        jdbcTemplate.update(
            "INSERT INTO Tasks(title, duedate, description) VALUES(?, ?, ?)",
            task.getTitle(),
            task.getDueDate(),
            task.getDescription()
        );
    }

    public void update(int id, Task updatedTask) {
        jdbcTemplate.update(
            "UPDATE Tasks SET title=?, dueDate=?, description=? WHERE id=?",
            updatedTask.getTitle(),
            updatedTask.getDueDate(),
            updatedTask.getDescription(),
            id
        );
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Tasks WHERE id=?", id);
    }

    public List<Task> getTodaysTasks() {
        return jdbcTemplate.query("SELECT * FROM Tasks WHERE duedate=?", new BeanPropertyRowMapper<>(Task.class), LocalDate.now());
    }

}
