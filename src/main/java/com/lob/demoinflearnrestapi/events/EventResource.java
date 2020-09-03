package com.lob.demoinflearnrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

// ResourceSupport -> 1.0.2^ RepresentationModel
// Resource -> EntityModel
// Resources -> CollectionModel
// PagedResources -> PagedModel
public class EventResource extends EntityModel<Event> {

    //EntityModel는 content 타입으로 값을 저장하는데 해당 getContent에 JsonUnwrapped 어노테이션이 이미 붙어있다.
    public EventResource(Event event, Link... links) {
        super(event, links);

        //add(new Link("http://localhost:8080/api/events" + event.getId()))와 동일한 작동 방식 밑의 방식이 더 Type safe하다.
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

/*  기존 방법
    // 반환되는 데이터 필드를 event라고 감싸지 않고 전송
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }*/

}
