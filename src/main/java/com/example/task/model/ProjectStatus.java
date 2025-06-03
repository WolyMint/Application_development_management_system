package com.example.task.model;

public enum ProjectStatus {
    OPEN("Открыт"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершён"),
    CLOSED("Закрыт");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}