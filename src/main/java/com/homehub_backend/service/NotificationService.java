package com.homehub_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyResident(UUID residentId, String message) {
        logger.info("Sending notification to resident: {}", residentId);
        String destination = "/topic/user-" + residentId;

        Map<String, Object> notification = Map.of(
                "type", "REQUEST_CREATED",
                "message", message,
                "timestamp", LocalDateTime.now().toString()
        );

        try {
            messagingTemplate.convertAndSend(destination, notification);
            logger.info("Successfully sent notification to resident {} at destination {}", residentId, destination);
        } catch (Exception e) {
            logger.error("Failed to send notification to resident {}: {}", residentId, e.getMessage(), e);
        }
    }

    public void notifyProvider(UUID providerId, String message) {
        logger.info("Sending notification to provider: {}", providerId);
        String destination = "/topic/provider-" + providerId;

        Map<String, Object> notification = Map.of(
                "type", "NEW_REQUEST_ASSIGNED",
                "message", message,
                "timestamp", LocalDateTime.now().toString(),
                "providerId", providerId.toString()
        );

        try {
            messagingTemplate.convertAndSend(destination, notification);
            logger.info("Successfully sent notification to provider {} at destination {}", providerId, destination);
            logger.debug("Notification content: {}", notification);
        } catch (Exception e) {
            logger.error("Failed to send notification to provider {}: {}", providerId, e.getMessage(), e);
        }
    }

    public void sendTestNotificationToProvider(UUID providerId) {
        logger.info("Sending test notification to provider: {}", providerId);
        notifyProvider(providerId, "This is a test notification to verify WebSocket connection");
    }

    public boolean isMessagingTemplateActive() {
        try {
            return messagingTemplate != null;
        } catch (Exception e) {
            logger.error("Messaging template health check failed: {}", e.getMessage());
            return false;
        }
    }
}