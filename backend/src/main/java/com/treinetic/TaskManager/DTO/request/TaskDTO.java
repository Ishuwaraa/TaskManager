package com.treinetic.TaskManager.DTO.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private String title;
    private String description;
    private String status;
}
