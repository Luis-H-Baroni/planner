package com.rocketseat.planner.trip.dtos;

import java.util.List;

public record TripReqDto(
        String destination,
        String startsAt,
        String endsAt,
        List<String> emailsToInvite,
        String ownerEmail,
        String ownerName)
{
}
