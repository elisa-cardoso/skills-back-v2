package com.example.skills_project.exception;

public class UserSkillNotFoundException extends RuntimeException {
    public UserSkillNotFoundException(String message) {
        super(message);
    }
}
