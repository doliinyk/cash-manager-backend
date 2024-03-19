package com.cashmanagerbackend.services;

import java.util.Map;

public interface EmailService {
    void sendMail(String to, String subject, String template, Map<String, Object> variables, String locale);
}
