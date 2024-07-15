package com.rocketseat.planner.link.dtos;

import java.util.UUID;

public record LinkResDto(
        UUID id,
        String title,
        String url
) {
}
