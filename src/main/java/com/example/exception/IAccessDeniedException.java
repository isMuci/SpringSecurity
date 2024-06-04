package com.example.exception;

import org.springframework.security.access.AccessDeniedException;

public class IAccessDeniedException extends AccessDeniedException {
    public IAccessDeniedException(String msg) {
        super(msg);

    }
}
