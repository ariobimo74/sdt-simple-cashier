package id.co.softwaredeveloperstoday.cms.dashboard.web.util.common;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class GoogleMailSenderUtil {

    private String emailSubject;
    private String emailContent;
    private String email;
    private String appPassword;
    private byte[] attachment;

    public void sendEmail(String recipientEmailAddresses) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmailAddresses));

            message.setSubject(emailSubject);

            Multipart multipart = new MimeMultipart("alternative");
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(emailContent, "text/html; charset=utf-8");
            multipart.addBodyPart(bodyPart);

            ByteArrayDataSource dataSource = new ByteArrayDataSource(attachment, "application/pdf");
            MimeBodyPart part = new MimeBodyPart();
            part.setDataHandler(new DataHandler(dataSource));
            part.setFileName("INVOICE.pdf");
            multipart.addBodyPart(part);

            message.setContent(multipart);

            Transport.send(message);
            log.info("Successfully sent mail to " + Arrays.stream(message.getAllRecipients()).map(String::valueOf).collect(Collectors.joining(", ")));
        } catch (MessagingException e) {
            log.error("Failed to send mail with root cause : " + e.getMessage());
        }
    }

}
