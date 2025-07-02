package attendanceUtils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class EmailSender {

    public static boolean sendEmailWithAttachment(String toEmail, String ccEmails, String subject, String body, String filePath) {
        final String fromEmail = System.getenv("EMAIL_USERNAME");
        final String password = System.getenv("EMAIL_PASSWORD");

        // ✅ Validate email environment variables
        if (fromEmail == null || fromEmail.trim().isEmpty()) {
            System.out.println("❌ EMAIL_USERNAME is not set in environment.");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("❌ EMAIL_PASSWORD is not set in environment.");
            return false;
        }
        if (toEmail == null || toEmail.trim().isEmpty()) {
            System.out.println("❌ Recipient (toEmail) is null or empty.");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            if (ccEmails != null && !ccEmails.trim().isEmpty()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmails));
            }

            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            // Email body
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(body);
            multipart.addBodyPart(textBodyPart);

            // Attachment
            if (filePath != null && new File(filePath).exists()) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(new File(filePath));
                multipart.addBodyPart(attachmentBodyPart);
                System.out.println("📎 Attached file: " + filePath);
            } else {
                System.out.println("⚠️ Attachment skipped: file path invalid or file doesn't exist.");
            }

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("✅ Email sent successfully to: " + toEmail);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
