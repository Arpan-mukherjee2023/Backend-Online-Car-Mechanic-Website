package com.backend.demo.DTO;

public class APIResponseDTO {
    private String message;
    private String error;

    public APIResponseDTO(String message) {
        this.message = message;
    }

    public APIResponseDTO(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
