package org.alpermelkeli.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;


@Aspect
@Component
public class ApiKeyAspect {
    @Value("${app.api.key}")
    private String validApiKey;

    @Before("@annotation(RequiresApiKey)")
    public void checkApiKey(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null || !apiKey.equals(validApiKey)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid API Key"
            );
        }
    }
}