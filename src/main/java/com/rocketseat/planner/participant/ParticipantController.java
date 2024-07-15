package com.rocketseat.planner.participant;

import com.rocketseat.planner.participant.dtos.ParticipantReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantRepository repository;

    public ParticipantController(ParticipantRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantReqDto payload) {
        Optional<Participant> participant = this.repository.findById(id);
        if(participant.isEmpty()) return ResponseEntity.notFound().build();

        Participant rawParticipant = participant.get();
        rawParticipant.setConfirmed(true);
        rawParticipant.setName(payload.name());

        Participant response = this.repository.save(rawParticipant);
        return ResponseEntity.ok(response);
    }
}
