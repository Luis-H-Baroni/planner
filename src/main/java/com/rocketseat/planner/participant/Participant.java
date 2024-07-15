package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    public Participant(String email, Trip trip){
        this.email = email;
        this.trip = trip;
        this.isConfirmed = false;
        this.name = "";
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed;

    @Column( nullable = false)
    private String email;
}