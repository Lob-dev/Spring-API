package com.lob.demoinflearnrestapi.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/events/") // perform = 받은 url 주소로 post 요청을 보냄 post -> /api/events/
                .contentType(MediaType.APPLICATION_JSON) // 이 요청의 본문에 JSON 타입을 보낸다.
                .accept(MediaTypes.HAL_JSON) // 받고 싶은 응답은 HAL_JSON을 원한다. (Hypertext Application Language)
                    )
                .andExpect(status().isCreated()); // 혹은 is(Status code 200, 201..)
    }

}
