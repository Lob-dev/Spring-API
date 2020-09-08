package com.lob.demoinflearnrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events/", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
    // value를 통해 이 안에 모든 핸들러들은 baseUrl을 가진다.
    // produces를 이용해서 이 클래스 안의 모든 핸들러들은 지정된 컨탠츠값으로 응답

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    //파라메터가 여러개 있어도 빈으로 등록되어있다면 @Autowried 같은 어노테이션이 없더라도 모두 주입된다.
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    //Location 헤더에 있는 URI를 통해 이벤트를 조회할 수 있다.
    //linkTo() 메소드는 컨트롤러나, 핸들러 메소드로부터 URI 정보 읽어올 때 쓰는 메소드이다.
    // /api/events/{id}를 uri로 변환하여서 linkTo를 통해 값을 읽어온 후 URI 타입의 변수에 저장한다.

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) { //ResponseEntity used in Spring MVC, return value from a @Controller method:
        //Dto를 사용할 경우 Repository를 사용하기 위해선 엔티티(Event)로 바꾸어야하는데 이때 사용할 수 있는 것이 DTO -> 도메인 객체로 값을 복사하는 ModelMapper이다.
        //이 방법을 사용하지 않는다면 도메인 객체를 직접 빌더하여서 evtentDto의 값을 하나씩 넣어줘야한다.
        //ModelMapper를 사용할 경우 리플렉션으로 인한 시간 지연이 발생한다. 이점을 고려해서 사용하자.
        // @Valid를 이용한 값 검증 후 에러 검출
        if (errors.hasErrors()) {
            //해당 코드는 에러가 뜬다 errors는 json으로 변환되지 않는다. 자바 Bean 스펙이 준수된 객체를 Json으로 변환해주는데 (BeanSerializer) errors는 스펙을 준수하지 않는다.
            return ResponseEntity.badRequest().body(errors);
        }
        // validator를 이용한 논리 값 검증
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);   //
        event.update();                                         // 서비스로 위임 대상
        Event newEvent = this.eventRepository.save(event);      // 서비스로 위임 대상

        //link 추가 부분
        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));  // 코드를 더 리팩토링 하겠다면. 링크를 생성하는 모든 메서드를
        eventResource.add(selfLinkBuilder.withRel("update-events"));               // EventResource 안에서 처리하는 방법이 있다.

        return ResponseEntity.created(createdUri).body(eventResource);


    }
}

