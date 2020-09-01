package com.lob.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.ExtendedBeanInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest 슬라이스 Test  WEB용 Bean들만 등록하여 주기 때문에 @Repository, extend JpaRepository 로 등록된 Bean은 적용되지 않는다.
@SpringBootTest // 통합 테스트를 이용하면 모든 빈들이 등록되므로 모킹을 하는 것을 많이 줄일 수 있다.
@AutoConfigureMockMvc //SpringBootTest를 사용했을 때 MockMvc를 주입받기 위해선 해당 어노테이션을 같이 사용하여야한다. 자동적으로 서블릿을 생성해준다.
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 애플리케이션 서버에 배포하지 않고도 스프링 MVC의 동작을 재현할 수 있는 클래스

    @Autowired
    private WebApplicationContext webApplicationContext; // 사용자 정의 DI 컨테이너를 만들기 위해 ApplicationContext를 확장한 WebApplicationContext을 자동으로 Inject 합니다.

    // Repository를 사용해야한다면 Mocking을 통해서 만들어 사용해야한다. 이 객체는 Mock 객체이기 때문에 어떠한 메서드를 이용해도 Null이 반환된다.
    // @MockBean EventRepository eventRepository; NullPointerException이 발생할 것이다. 이를 해결하기 위해선 어떻게 동작하라고 지정을 해야한다.

/*
    @Before // 테스트 실행전 이 어노테이션이 선언된 부분을 실행한다.
    public void setup() { // Filters는 서블렛 요청와 응답사이에 존재하여 필터의 설정을 통하여 요청들을 뽑아내고 조작할 수 있다.
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext) // 스프링 MVC의 설정을 적용한 DI 컨테이너를 만들어 이 DI 컨테이너를 사용해 스프링 MVC 동작 재현
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // Location 의 반환 값인 한글이 깨지지 않도록 인코딩해주는 필터를 추가한다.
                .build();
    }
*/

    // 스프링 부트 사용시 Jackson이라는 json Mapper가 이미 bean으로 등록되어 있으므로 새로운 bean 등록 없이도 ObjectMapper를 사용할 수 있다
    @Autowired
    ObjectMapper objectMapper;

    // 작동 흐름
    // (client) MockHttpServletRequest (Post) -> doFilters -> DispatcherServlet -> preHandler -> AOP(Around) EventController의 (Post)createEvent(Event)
    // -> postHandler -> DispatcherServlet -> doFilters-> (client) MockHttpServletResponse (status 201)

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder() // Event 엔티티 제작 client 의 Form 을 통한 Post 요청과 동일
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21)) // 모집일
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21)) // 모집 종료일
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21)) // 시작일
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21)) // 종료일
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100) // 최대 인원
                .location("강남역 D2 스타텁 팩토리") // 장소
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        // 입력값 제한이란? 받기 원하는 값들만 받을 수 있도록 제약을 거는 것
        // 계산되어야하는 값, 시스템을 통해 지정해야 되는 값은 입력값을 제한해야 한다.
        // 여기에서는 Id(DB 생성), free(basePrice, maxPrice가 없어야함), offline 로직을 통한 판단이 필요한 것들
        mockMvc.perform(post("/api/events/") // perform = 받은 url 주소로 post 요청을 보냄 post -> /api/events/
                    .contentType(MediaType.APPLICATION_JSON) // 이 요청의 본문에 JSON 타입을 보낸다.
                    .accept(MediaTypes.HAL_JSON) // 받고 싶은 응답은 HAL_JSON을 원한다. (Hypertext Application Language)
                    .content(objectMapper.writeValueAsString(event)))// jackson 을 이용하여 해당 엔티티를 json 문자열로 변환 후 요청 본문에 넣었다.
                .andDo(print()) // andDo(print())를 통하여 실제 요청과 응답을 확인할 수 있다.
                .andExpect(status().isCreated()) // Created는 201이다. 이와 다른 방법으론 is(201) 등으로 직접 입력하면 된다.
                .andExpect(jsonPath("id").exists()) // id 라는 응답 값이 있는지 확인
                .andExpect(header().exists(HttpHeaders.LOCATION)) // Location 응답 헤더 확인
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)) // Content-Type 응답 헤더 확인
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)));
                //andDo(print())를 통해 나온 모든 값들은 andExpect로 검증할 수 있다.
                //직접 문자열로 타입을 넣는 것보다 HttpHeaders, MediaTypes 등을 이용해 상수를 이용해서 좀 더 완벽한 검증을 할 수 있다.
                //Dto를 사용하여 이 문제를 해결해보자. Jackson to json 제공하는 어노테이션 @JsonIgnore 등도 사용할 수 있으나.
                //어노테이션이 너무 많으면 복잡하게 엮일 수 있기때문에 분산하기로 하였다. DTO의 문제점은 필드의 중복이 발생한다는 것이다.


    }

}
