package com.rocketseat.planner.link;

import com.rocketseat.planner.link.dtos.LinkCreateResDto;
import com.rocketseat.planner.link.dtos.LinkReqDto;
import com.rocketseat.planner.link.dtos.LinkResDto;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    private final LinkRepository repository;

    public LinkService(LinkRepository repository) {
        this.repository = repository;
    }

    public LinkCreateResDto createLink(LinkReqDto payload, Trip rawTrip) {
        Link link = new Link(payload.title(), payload.url(), rawTrip);

        this.repository.save(link);

        return new LinkCreateResDto(link.getId());
    }

    public List<LinkResDto> getLinksFromTrip(UUID tripId){
        return this.repository.findByTripId(tripId)
                .stream()
                .map(link -> new LinkResDto(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
