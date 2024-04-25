package org.example.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private int id;

    @NotEmpty(message = "The title must not be empty")
    @Size(min = 3, message = "The title must be at least 3 characters long")
    private String title;

    @Size(max = 256, message = "The description must be less than 256 characters")
    private String description;

    private LocalDate dueDate;

}
