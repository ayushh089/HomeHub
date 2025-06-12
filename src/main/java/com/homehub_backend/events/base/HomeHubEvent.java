package com.homehub_backend.events.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class HomeHubEvent extends ApplicationEvent {
    private final String eventId;
    private final LocalDateTime occurredAt;
    private final UUID userId;

    public HomeHubEvent(Object source, UUID userId) {
        super(source);
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = LocalDateTime.now();
        this.userId = userId;
    }


}
