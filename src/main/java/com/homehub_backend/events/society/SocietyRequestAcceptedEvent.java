package com.homehub_backend.events.society;

import com.homehub_backend.events.base.HomeHubEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SocietyRequestAcceptedEvent extends HomeHubEvent {
    private final UUID societyId;
    private final String societyName;
    private final String adminEmail;
    private LocalDateTime acceptedDate;

    public SocietyRequestAcceptedEvent(Object source, UUID userId, UUID societyId,
            String societyName, String adminEmail,LocalDateTime acceptedDate) {
        super(source, userId);
        this.societyId = societyId;
        this.societyName = societyName;
        this.adminEmail = adminEmail;
        this.acceptedDate = acceptedDate;
    }
}
