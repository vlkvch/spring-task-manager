package org.example.controller.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.example.dao.TaskDAO;
import org.example.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;

import jakarta.validation.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TasksApiController {

    @Autowired
    private final TaskDAO taskDAO;

    @Autowired
    private final ObjectMapper objectMapper;

    /**
     * Adds a task.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public String add(@RequestBody @Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return String.format(
                "\"errors\":%s",
                objectMapper.convertValue(
                    bindingResult
                        .getAllErrors()
                        .stream()
                        .map(e -> e.getDefaultMessage())
                        .toList(),
                    JsonNode.class
                )
            );
        }

        taskDAO.add(task);
        return "{\"errors\":[]}";
    }

    /**
     * Returns all the tasks.
     */
    @GetMapping
    public List<Task> getAll() {
        return taskDAO.getAll();
    }

    /**
     * Returns a task based on its ID.
     */
    @GetMapping("/{id:\\d+}")
    public Task getById(@PathVariable("id") int id) {
        return taskDAO.getById(id);
    }

    /**
     * Gets today's tasks.
     */
    @GetMapping("/today")
    public List<Task> today() {
        return taskDAO.getTodaysTasks();
    }

    /**
     * Gets tasks by their due date.
     *
     * The format is "YYYY-MM-DD".
     */
    @GetMapping("/{date:\\d{4}-\\d{2}-\\d{2}}")
    public List<Task> getByDate(@PathVariable("date") LocalDate date) {
        return taskDAO.getByDueDate(date);
    }

    /**
     * Updates a task based on its ID.
     */
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json", produces = "application/json")
    public String update(@PathVariable("id") int id, @RequestBody JsonPatch patch) {
        try {

            JsonNode patched = patch.apply(objectMapper.convertValue(taskDAO.getById(id), JsonNode.class));
            Task updatedTask = objectMapper.treeToValue(patched, Task.class);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Task>> violations = validator.validate(updatedTask);

            if (!violations.isEmpty()) {
                return String.format(
                    "\"errors\":%s",
                    objectMapper.convertValue(
                        violations
                            .stream()
                            .map(v -> v.getMessage())
                            .toList(),
                        JsonNode.class
                    )
                );
            }

            taskDAO.update(id, updatedTask);
            return "{\"errors\":[]}";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deletes a task based on its ID.
     */
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public String delete(@PathVariable("id") int id) {
        taskDAO.delete(id);
        return "{\"errors\":[]}";
    }

}
