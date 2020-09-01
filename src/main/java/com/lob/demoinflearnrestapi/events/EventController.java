package com.lob.demoinflearnrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events/", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
    // value를 통해 이 안에 모든 핸들러들은 baseUrl을 가진다.
    // produces를 이용해서 이 클래스 안의 모든 핸들러들은 지정된 컨탠츠값으로 응답

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    //Location 헤더에 있는 URI를 통해 이벤트를 조회할 수 있다.
    //linkTo() 메소드는 컨트롤러나, 핸들러 메소드로부터 URI 정보 읽어올 때 쓰는 메소드이다.
    // /api/events/{id}를 uri로 변환하여서 linkTo를 통해 값을 읽어온 후 URI 타입의 변수에 저장한다.
    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) { //ResponseEntity used in Spring MVC, return value from a @Controller method:
        //Dto를 사용할 경우 Repository를 사용하기 위해선 엔티티(Event)로 바꾸어야하는데 이때 사용할 수 있는 것이 DTO -> 도메인 객체로 값을 복사하는 ModelMapper이다.
        //이 방법을 사용하지 않는다면 도메인 객체를 직접 빌더하여서 evtentDto의 값을 하나씩 넣어줘야한다.
        //ModelMapper를 사용할 경우 리플렉션으로 인한 시간 지연이 발생한다. 이점을 고려해서 사용하자.

        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);

        // event.setId(10) 임의로 엔티티에 setter를 통해 id를 저장 id를 반환하는 것은 DB에 정상적으로 저장했다는 것을 알리기도 하지만 해당 id를 이용해 값을 조회할 때 쓰기도 함
    }
}

