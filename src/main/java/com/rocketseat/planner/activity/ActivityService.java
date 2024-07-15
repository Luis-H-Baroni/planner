package com.rocketseat.planner.activity;

import com.rocketseat.planner.activity.dtos.ActivityCreateResDto;
import com.rocketseat.planner.activity.dtos.ActivityReqDto;
import com.rocketseat.planner.activity.dtos.ActivityResDto;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository repository;

    public ActivityService(ActivityRepository repository) {
        this.repository = repository;
    }

    public ActivityCreateResDto createActivity(ActivityReqDto payload, Trip trip){
        Activity activity = new Activity(payload.title(), payload.startsAt(), trip);

        this.repository.save(activity);

        return new ActivityCreateResDto(activity.getId());
    }

    public List<ActivityResDto> getActivitiesFromTrip(UUID tripId) {
        return this.repository.findByTripId(tripId)
                .stream()
                .map(activity -> new ActivityResDto(activity.getId(), activity.getTitle(), activity.getStartsAt())).toList();


    }
}
