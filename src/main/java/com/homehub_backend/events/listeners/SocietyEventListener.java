package com.homehub_backend.events.listeners;

import com.homehub_backend.events.society.SocietyCreateRequestEvent;
import com.homehub_backend.events.society.SocietyRegisteredEvent;
import com.homehub_backend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocietyEventListener {

    @Autowired
    private EmailService emailService;

//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private AuditService auditService;

//    @EventListener
//    public void handleSocietyRegistered(SocietyRegisteredEvent event) {
//        log.info("Handling SocietyRegisteredEvent for society: {}", event.getSocietyName());
//
//        // Send welcome email to admin
//        emailService.sendWelcomeEmail(
//                event.getAdminEmail(),
//                event.getSocietyName(),
//                event.getSocietyId()
//        );
//
//        // Create society admin dashboard
//        notificationService.createAdminDashboard(event.getSocietyId());
//
//        // Log audit event
//        auditService.logEvent(
//                "SOCIETY_REGISTERED",
//                event.getSocietyId(),
//                event.getUserId(),
//                "Society " + event.getSocietyName() + " registered successfully"
//        );
//    }

    @EventListener
    public void handleSocietyRegistered(SocietyCreateRequestEvent event) {
        log.info("Handling SocietyRegisteredEvent for society: {}", event.getSocietyName());

        // Send welcome email to admin
        emailService.sendSocietyRequestMail(
                event.getAdminEmail(),
                event.getSocietyName(),
                event.getSocietyId()
        );

        // Create society admin dashboard
//        notificationService.createAdminDashboard(event.getSocietyId());
//
//        // Log audit event
//        auditService.logEvent(
//                "SOCIETY_REGISTERED",
//                event.getSocietyId(),
//                event.getUserId(),
//                "Society " + event.getSocietyName() + " registered successfully"
//        );
    }

//    @EventListener
//    public void handleSocietyUpdated(SocietyUpdatedEvent event) {
//        log.info("Handling SocietyUpdatedEvent for society: {}", event.getSocietyId());
//
//        // Notify all residents about society updates
//        notificationService.notifyAllResidents(
//                event.getSocietyId(),
//                "Society information has been updated"
//        );
//
//        // Log audit event
//        auditService.logEvent(
//                "SOCIETY_UPDATED",
//                event.getSocietyId(),
//                event.getUserId(),
//                "Society information updated"
//        );
//    }
//
//    @EventListener
//    public void handleSocietyDeactivated(SocietyDeactivatedEvent event) {
//        log.info("Handling SocietyDeactivatedEvent for society: {}", event.getSocietyId());
//
//        // Send deactivation notification
//        emailService.sendDeactivationEmail(
//                event.getAdminEmail(),
//                event.getSocietyName(),
//                event.getReason()
//        );
//
//        // Suspend all active service requests
//        notificationService.suspendAllActiveRequests(event.getSocietyId());
//
//        auditService.logEvent(
//                // Log audit event
//                "SOCIETY_DEACTIVATED",
//                event.getSocietyId(),
//                event.getUserId(),
//                "Society deactivated. Reason: " + event.getReason()
//        );
//    }
}