package com.homehub_backend.events.society;

import com.homehub_backend.events.base.HomeHubEvent;

import java.util.UUID;


public class ServiceRequestCreatedEvent extends HomeHubEvent {
    private final UUID requestId;
    private final UUID residentId;
    private final String serviceType;
    private final String description;
    private final UUID societyId;


    public ServiceRequestCreatedEvent(Object source, UUID residentId, UUID requestId,
                                      String serviceType, String description, UUID societyId) {
        super(source, residentId);
        this.requestId = requestId;
        this.residentId = residentId;
        this.serviceType = serviceType;
        this.description = description;
        this.societyId = societyId;
    }

// getters...


}