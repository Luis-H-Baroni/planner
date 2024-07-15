package com.rocketseat.planner.participant.dtos;

import java.util.UUID;

public record ParticipantResDto(
        UUID id,
        String name,
        String email,
        Boolean isConfirmed
        ) {
}
