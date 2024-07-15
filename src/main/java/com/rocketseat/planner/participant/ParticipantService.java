package com.rocketseat.planner.participant;

import com.rocketseat.planner.participant.dtos.ParticipantCreateResDto;
import com.rocketseat.planner.participant.dtos.ParticipantResDto;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    private final ParticipantRepository repository;

    public ParticipantService(ParticipantRepository repository) {
        this.repository = repository;
    }

    public void registerParticipantsToTrip(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.repository.saveAll(participants);
    }

    public ParticipantCreateResDto registerParticipantToTrip(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        this.repository.save(participant);

        return new ParticipantCreateResDto(participant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {}

    public void triggerConfirmationEmailToParticipant(String email) {}

    public List<ParticipantResDto> getParticipantsFromTrip(UUID tripId) {
        return this.repository.findByTripId(tripId)
                .stream()
                .map(participant -> new ParticipantResDto(participant.getId(), participant.getName(), participant.getEmail(), participant.isConfirmed())).toList();
    }
}
