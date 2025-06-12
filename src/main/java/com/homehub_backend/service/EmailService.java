package com.homehub_backend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:HomeHub}")
    private String appName;

    @Value("${app.url:http://localhost:8081}")
    private String appUrl;

    @Async
    public CompletableFuture<Void> sendVerificationEmail(String toEmail, String verificationCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setFrom(fromEmail);
            helper.setSubject(appName + " - Email Verification");

            String htmlContent = buildVerificationEmailTemplate(verificationCode, appName, appUrl);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            System.out.println("Verification email sent successfully to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send verification email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }

        return CompletableFuture.completedFuture(null);
    }


    @Async
    public CompletableFuture<Void> sendWelcomeEmail(String toEmail, String firstName, String role) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setFrom(fromEmail);
            helper.setSubject("Welcome to " + appName + "!");

            String htmlContent = buildWelcomeEmailTemplate(firstName, role, appName, appUrl);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            System.out.println("Welcome email sent successfully to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send welcome email to " + toEmail + ": " + e.getMessage());
            // Don't throw exception for welcome email failure
        }

        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> sendSocietyRequestMail(String adminEmail, String societyName, UUID societyId) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(adminEmail);
            helper.setFrom(fromEmail);
            helper.setSubject("Welcome to " + appName + "!");

            String htmlContent = buildSocietyUserConfirmationTemplate(societyName, appName, appUrl);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            System.out.println("Welcome email sent successfully to: " + adminEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send welcome email to " + adminEmail + ": " + e.getMessage());
            // Don't throw exception for welcome email failure
        }

        return CompletableFuture.completedFuture(null);

    }


    private String buildVerificationEmailTemplate(String verificationCode, String appName, String appUrl) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Email Verification</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }
                        .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        .header { text-align: center; margin-bottom: 30px; }
                        .logo { font-size: 28px; font-weight: bold; color: #2563eb; margin-bottom: 10px; }
                        .verification-code { font-size: 32px; font-weight: bold; color: #1f2937; background-color: #f3f4f6; padding: 15px; border-radius: 8px; text-align: center; margin: 20px 0; letter-spacing: 3px; }
                        .content { color: #374151; line-height: 1.6; }
                        .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e5e7eb; font-size: 14px; color: #6b7280; text-align: center; }
                              .button {
                                font-weight: 500;
                                display: inline-block;
                                background-color: #5e8ae9;
                                color: white !important;
                                padding: 12px 24px;
                                text-decoration: none;
                                border-radius: 6px;
                                margin: 20px 0;
                              }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <div class="logo">%1$s</div>
                            <h1 style="color: #1f2937; margin: 0;">Verify Your Email Address</h1>
                        </div>
                
                        <div class="content">
                            <p>Thank you for signing up with %1$s! To complete your registration, please verify your email address by entering the verification code below:</p>
                
                            <div class="verification-code">%2$s</div>
                
                            <p><strong>Important:</strong></p>
                            <ul>
                                <li>This code will expire in 10 minutes</li>
                                <li>Enter this code exactly as shown</li>
                                <li>If you didn't request this, please ignore this email</li>
                            </ul>
                
                            <p>If you're having trouble, you can also click the button below to verify your email:</p>
                            <div style="text-align: center;">
                                <a href="%3$s/auth/verify-email?code=%2$s" class="button">Verify Email</a>
                            </div>
                        </div>
                
                        <div class="footer">
                            <p>This email was sent from %1$s. If you didn't request this email, please ignore it.</p>
                            <p>&copy; 2024 %1$s. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """, appName, verificationCode, appUrl);
    }


    private String buildWelcomeEmailTemplate(String firstName, String role, String appName, String appUrl) {
        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Welcome to %1$s</title>
                            <style>
                                body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }
                                .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                                .header { text-align: center; margin-bottom: 30px; }
                                .logo { font-size: 28px; font-weight: bold; color: #2563eb; margin-bottom: 10px; }
                                .welcome-banner { background: linear-gradient(135deg, #2563eb, #3b82f6); color: white; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0; }
                                .content { color: #374151; line-height: 1.6; }
                                .feature-list { background-color: #f9fafb; padding: 20px; border-radius: 8px; margin: 20px 0; }
                                .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e5e7eb; font-size: 14px; color: #6b7280; text-align: center; }
                                .button { display: inline-block; background-color: #2563eb; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; margin: 20px 0; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <div class="logo">%1$s</div>
                                </div>
                        
                                <div class="welcome-banner">
                                    <h1 style="margin: 0; font-size: 24px;">Welcome to %1$s, %2$s! 🎉</h1>
                                    <p style="margin: 10px 0 0 0; opacity: 0.9;">Your %3$s account is now active</p>
                                </div>
                        
                                <div class="content">
                                    <p>Congratulations! Your email has been verified and your account is now fully activated. You're all set to start using %1$s.</p>
                        
                                    <div class="feature-list">
                                        <h3 style="margin-top: 0; color: #1f2937;">What you can do now:</h3>
                                        <ul>
                                            <li>Complete your profile setup</li>
                                            <li>Explore the dashboard</li>
                                            <li>Connect with your community</li>
                                            <li>Access all %3$s features</li>
                                        </ul>
                                    </div>
                        
                                    <p>Ready to get started? Click the button below to access your dashboard:</p>
                                    <div style="text-align: center;">
                                        <a href="%4$s/dashboard" class="button">Go to Dashboard</a>
                                    </div>
                        
                                    <p>If you have any questions or need assistance, feel free to reach out to our support team.</p>
                                </div>
                        
                                <div class="footer">
                                    <p>Thank you for choosing %1$s!</p>
                                    <p>&copy; 2024 %1$s. All rights reserved.</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                appName,
                firstName,
                role.toLowerCase(),
                appUrl
        );
    }

    private String buildSocietyUserConfirmationTemplate(String societyName, String appName, String appUrl) {
        return String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Society Registration Received</title>
            <style>
                body { font-family: Arial, sans-serif; background-color: #f3f4f6; margin: 0; padding: 20px; }
                .container { max-width: 600px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
                .header { text-align: center; }
                .header h1 { color: #2563eb; }
                .content { margin-top: 20px; color: #374151; font-size: 16px; line-height: 1.6; }
                .society-name { font-size: 20px; font-weight: bold; color: #111827; }
                .footer { margin-top: 30px; font-size: 13px; color: #6b7280; text-align: center; border-top: 1px solid #e5e7eb; padding-top: 20px; }
                .button {
                    display: inline-block;
                    margin-top: 20px;
                    background-color: #2563eb;
                    color: white !important;
                    padding: 12px 24px;
                    text-decoration: none;
                    border-radius: 6px;
                    font-weight: 500;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>%2$s</h1>
                </div>
                <div class="content">


                    <p>Thank you for registering your society with us. We have received your request for the following society:</p>
                    <p class="society-name">%1$s</p>

                    <p>Our team will now verify the details, and you will be notified once the request is approved. This typically takes 1–2 business days.</p>

                    <p>If you have any questions, feel free to contact us anytime.</p>

                    <a href="%3$s" class="button">Visit %2$s</a>
                </div>

                <div class="footer">
                    &copy; 2024 %2$s. All rights reserved.
                </div>
            </div>
        </body>
        </html>
    """, societyName, appName, appUrl);
    }


}
