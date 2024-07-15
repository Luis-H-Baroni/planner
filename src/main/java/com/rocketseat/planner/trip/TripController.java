package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.activity.dtos.ActivityCreateResDto;
import com.rocketseat.planner.activity.dtos.ActivityReqDto;
import com.rocketseat.planner.activity.dtos.ActivityResDto;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.link.dtos.LinkCreateResDto;
import com.rocketseat.planner.link.dtos.LinkReqDto;
import com.rocketseat.planner.link.dtos.LinkResDto;
import com.rocketseat.planner.participant.ParticipantService;
import com.rocketseat.planner.participant.dtos.ParticipantCreateResDto;
import com.rocketseat.planner.participant.dtos.ParticipantReqDto;
import com.rocketseat.planner.participant.dtos.ParticipantResDto;
import com.rocketseat.planner.trip.dtos.TripReqDto;
import com.rocketseat.planner.trip.dtos.TripResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final ParticipantService participantService;

    private final ActivityService activityService;

    private final LinkService linkService;

    private final TripRepository repository;

    public TripController(ParticipantService participantService, ActivityService activityService, LinkService linkService, TripRepository repository) {
        this.participantService = participantService;
        this.activityService = activityService;
        this.linkService = linkService;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<TripResDto> createTrip(@RequestBody TripReqDto payload){
        Trip newTrip = new Trip(payload);

        this.repository.save(newTrip);
        this.participantService.registerParticipantsToTrip(payload.emailsToInvite(), newTrip);

        return ResponseEntity.ok(new TripResDto(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripReqDto payload){
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) return ResponseEntity.notFound().build();

        Trip rawTrip = trip.get();
        rawTrip.setDestination(payload.destination());
        rawTrip.setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));

        Trip response = this.repository.save(rawTrip);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) return ResponseEntity.notFound().build();

        Trip rawTrip = trip.get();
        rawTrip.setConfirmed(true);

        Trip response = this.repository.save(rawTrip);
        this.participantService.triggerConfirmationEmailToParticipants(rawTrip.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResDto> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantReqDto payload){
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) return ResponseEntity.notFound().build();

        Trip rawTrip = trip.get();

        ParticipantCreateResDto response = this.participantService.registerParticipantToTrip(payload.email(), rawTrip);

        if(rawTrip.isConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantResDto>> getParticipants(@PathVariable UUID id){
        List<ParticipantResDto> participants = this.participantService.getParticipantsFromTrip(id);

        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityCreateResDto> createActivity(@PathVariable UUID id, @RequestBody ActivityReqDto payload){
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) return ResponseEntity.notFound().build();

        Trip rawTrip = trip.get();

        ActivityCreateResDto response = this.activityService.createActivity(payload, rawTrip);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityResDto>> getActivities(@PathVariable UUID id){
        List<ActivityResDto> activities = this.activityService.getActivitiesFromTrip(id);

        return ResponseEntity.ok(activities);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkCreateResDto> createLink(@PathVariable UUID id, @RequestBody LinkReqDto payload){
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) return ResponseEntity.notFound().build();

        Trip rawTrip = trip.get();

        LinkCreateResDto response = this.linkService.createLink(payload, rawTrip);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkResDto>> getLinks(@PathVariable UUID id){
        List<LinkResDto> links = this.linkService.getLinksFromTrip(id);

        return ResponseEntity.ok(links);
    }
}
