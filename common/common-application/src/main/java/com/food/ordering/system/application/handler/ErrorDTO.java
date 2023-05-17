package com.food.ordering.system.application.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {
    private final String code;
    private final String message;
}
