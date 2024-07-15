package com.rocketseat.planner.activity.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityResDto(
        UUID id, String title, LocalDateTime startsAt
) {
}
