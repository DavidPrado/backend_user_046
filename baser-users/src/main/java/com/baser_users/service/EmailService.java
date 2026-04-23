package com.baser_users.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public Mono<Void> sendPasswordResetEmail(String to, String token) {
        return Mono.defer(() -> {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                String htmlContent = generatePasswordResetHtml(token);

                helper.setTo(to);
                helper.setSubject("Recuperação de Senha - ETEC");
                helper.setText(htmlContent, true);

                mailSender.send(mimeMessage);
                return Mono.empty(); // Sinaliza conclusão com sucesso
            } catch (Exception e) {
                return Mono.error(e); // Sinaliza erro real
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private String generatePasswordResetHtml(String token) {
        String url = "http://localhost:8080/reset-password?token=" + token;

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                .container { font-family: sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; }
                .header { background-color: #b11116; color: white; padding: 20px; text-align: center; }
                .content { padding: 30px; line-height: 1.6; color: #333; }
                .token-box { background-color: #f4f4f4; padding: 15px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px; margin: 20px 0; border-radius: 4px; border: 1px dashed #b11116; }
                .button { display: inline-block; padding: 12px 24px; background-color: #b11116; color: #ffffff !important; text-decoration: none; border-radius: 5px; font-weight: bold; margin-top: 20px; }
                .footer { background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #777; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h2>ETEC - Recuperação de Conta</h2>
                </div>
                <div class="content">
                    <p>Olá,</p>
                    <p>Recebemos uma solicitação para redefinir sua senha. Use o código abaixo para prosseguir:</p>
                    <div class="token-box">%s</div>
                    <p>Ou, se preferir, clique no botão abaixo para redefinir diretamente:</p>
                    <div style="text-align: center;">
                        <a href="%s" class="button">Redefinir Minha Senha</a>
                    </div>
                    <p>Se você não solicitou essa alteração, ignore este e-mail.</p>
                </div>
                <div class="footer">
                    &copy; 2024 ETEC. Todos os direitos reservados.
                </div>
            </div>
        </body>
        </html>
        """.formatted(token, url); // Injeta as variáveis na ordem dos %s
    }
}