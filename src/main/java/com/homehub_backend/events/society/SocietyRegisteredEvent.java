package com.homehub_backend.events.society;

import com.homehub_backend.events.base.HomeHubEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SocietyRegisteredEvent extends HomeHubEvent {
    private final UUID societyId;
    private final String societyName;
    private final String adminEmail;

    public SocietyRegisteredEvent(Object source, UUID userId, UUID societyId,
                                  String societyName, String adminEmail) {
        super(source, userId);
        this.societyId = societyId;
        this.societyName = societyName;
        this.adminEmail = adminEmail;
    }



}