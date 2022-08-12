package com.chapter.backend.services.iamservice.services.impl;

import com.chapter.backend.services.iamservice.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public MailServiceImpl(final JavaMailSender mailSender,
                           final SpringTemplateEngine templateEngine) {
        this.emailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendEmail(String subject, String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    @Async
    public void sendEmail(String subject, String mailTo, String template, Map<String, Object> mailProps) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
            messageHelper.setTo(mailTo);
            messageHelper.setText(templateEngine.process(template, new Context(Locale.getDefault(), mailProps)), true);
            messageHelper.setSubject(subject);
            messageHelper.setFrom("setMailFrom");
            emailSender.send(message);
        } catch (MessagingException e) {
            //TODO: Send notification: email could not be sent
            log.error("Could not send email to activate account");
        }
    }
}