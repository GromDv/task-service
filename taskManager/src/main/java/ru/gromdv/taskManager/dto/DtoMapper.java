package ru.gromdv.taskManager.dto;

import org.springframework.stereotype.Component;
import ru.gromdv.taskManager.model.Task;

@Component
public class DtoMapper {
    /**
     * Преобразование в dto.
     *
     * @param task объект задачи.
     * @return объект dto.
     */
    public TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setDateCreate(task.getDateCreate());
        taskDto.setDateCreateGood(task.getDateCreate());
        taskDto.setStatus(task.getStatus());
        taskDto.setStatusGood(task.getStatus());
        taskDto.setDateComplete(task.getDateComplete());
        taskDto.setDateCompleteGood(task.getDateComplete());
        return taskDto;
    }

    /**
     * Преобразование в объект сущности.
     *
     * @param taskDto объект dto.
     * @return объект сущности.
     */
    public Task toEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDateCreate(taskDto.getDateCreate());
        task.setStatus(taskDto.getStatus());
        task.setDateComplete(taskDto.getDateComplete());
        return task;
    }
}
