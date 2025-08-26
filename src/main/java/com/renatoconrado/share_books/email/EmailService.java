package com.renatoconrado.share_books.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(
        String to,
        String username,
        EmailTemplate.Name emailTemplate,
        String confirmationUrl,
        String Activationcode,
        String subject
    ) throws MessagingException {
        emailTemplate = emailTemplate == null
                        ? EmailTemplate.Name.CONFIRM_EMAIL
                        : EmailTemplate.Name.ACTIVATE_ACCOUNT;

        var mimeMessage = this.mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(
            mimeMessage,
            MimeMessageHelper.MULTIPART_MODE_MIXED,
            StandardCharsets.UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>(Map.of(
            "username", username,
            "confirmationUrl", confirmationUrl,
            "activation_code", Activationcode
        ));

        var context = new Context(Locale.getDefault(), properties);

        helper.setFrom("contact@renato.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = this.templateEngine.process(emailTemplate.toString(), context);

        helper.setText(template, true);

        this.mailSender.send(mimeMessage);
    }
}
