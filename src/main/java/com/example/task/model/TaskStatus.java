package com.example.task.model;

import lombok.Getter;

@Getter
public enum TaskStatus {
    OPEN("Открыт"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершён"),
    REJECTED("Отклонено");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }
}
