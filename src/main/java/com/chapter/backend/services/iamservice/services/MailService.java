package com.chapter.backend.services.iamservice.services;

import java.util.Map;

public interface MailService {

    void sendEmail(String subject, String to, String text);

    void sendEmail(String subject, String mailTo, String template, Map<String, Object> mailProps);
}