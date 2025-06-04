package com.homehub_backend.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    private boolean success;
    private String message;
    private String nextStep;
    private Integer completionPercentage;

    public static ProfileResponse complete() {
        return ProfileResponse.builder()
                .success(true)
                .message("Profile completed successfully")
                .nextStep("COMPLETE")
                .completionPercentage(100)
                .build();
    }

    public static ProfileResponse nextStep(String step, int percentage) {
        return ProfileResponse.builder()
                .success(true)
                .message("Profile updated successfully")
                .nextStep(step)
                .completionPercentage(percentage)
                .build();
    }
}
