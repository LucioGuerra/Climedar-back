package com.climedar.library.dto;

public record ApiError(String code, String message, Integer status) {
}
