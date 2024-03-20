package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String fromMailUsername;

    @Async
    @Override
    public void sendMail(String to, String subject, String template, Map<String, Object> variables, String locale) {
        MimeMessage message = createMessage(to, subject, template, variables, locale);

        mailSender.send(message);
    }

    private MimeMessage createMessage(String to,
                                      String subject,
                                      String template,
                                      Map<String, Object> variables,
                                      String localeCode) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            Locale locale = localeCode != null ? Locale.forLanguageTag(localeCode.toLowerCase()) : Locale.ROOT;
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            Context context = new Context(locale);

            mimeMessageHelper.setSubject(messageSource.getMessage(subject + ".subject", null, locale));
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(fromMailUsername);
            context.setVariables(variables);

            String text = templateEngine.process(template, context);
            mimeMessageHelper.setText(text, true);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send mail message");
        }

        return message;
    }
}
