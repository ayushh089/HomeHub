package com.homehub_backend.events.society;

import com.homehub_backend.events.base.HomeHubEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class ServiceRequestAcceptedEvent extends HomeHubEvent {
    private final UUID requestId;
    private final UUID serviceProviderId;
    private final UUID residentId;
    private final LocalDateTime acceptedAt;

    public ServiceRequestAcceptedEvent(Object source, UUID serviceProviderId,
                                       UUID requestId, UUID residentId) {
        super(source, serviceProviderId);
        this.requestId = requestId;
        this.serviceProviderId = serviceProviderId;
        this.residentId = residentId;
        this.acceptedAt = LocalDateTime.now();
    }

// getters...


}