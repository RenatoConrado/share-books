package com.renatoconrado.share_books.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${application.security.mail.contact-email}")
    private String CONTACT_EMAIL;

    @Async
    public void sendEmail(
        String to,
        String username,
        EmailTemplate.Name emailTemplate,
        String confirmationUrl,
        String activationCode,
        String subject
    ) throws MessagingException {

        var mimeMessage = this.mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(
            mimeMessage,
            MimeMessageHelper.MULTIPART_MODE_MIXED,
            StandardCharsets.UTF_8.name()
        );

        helper.setFrom(this.CONTACT_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);

        String template = this.buildTemplate(
            emailTemplate,
            username,
            confirmationUrl,
            activationCode
        );

        helper.setText(template, true);

        this.mailSender.send(mimeMessage);
    }

    private String buildTemplate(
        EmailTemplate.Name templateName,
        String username,
        String confirmationUrl,
        String activationCode
    ) {
        templateName = templateName == null
                       ? EmailTemplate.Name.CONFIRM_EMAIL
                       : EmailTemplate.Name.ACTIVATE_ACCOUNT;

        Map<String, Object> properties = Map.of(
            "username", username,
            "confirmationUrl", confirmationUrl,
            "activation_code", activationCode
        );
        var context = new Context(Locale.getDefault(), properties);

        return this.templateEngine.process(templateName.toString(), context);
    }
}
