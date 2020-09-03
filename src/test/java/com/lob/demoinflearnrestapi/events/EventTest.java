package com.lob.demoinflearnrestapi.events;

import com.sun.xml.fastinfoset.stax.events.EventBase;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    //JunitParams ?

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }


    // java bean 스펙을 준수하나요? Default 생성자, Getter, Setter로 생성이 가능한가?
    @Test
    public void javaBean() {
        //given
        String name = "Event";
        String description = "Spring";

        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //That
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    /*
      @Parameters에 선언된 배열을 순차적으로 인자로 전달하여 (인자 값을 통한 논리) 테스트를 실행
      중복되는 테스트를 줄일 수 있고, 깨진 케이스만을 알 수 있다.

      방법 1
      인자에 배열처럼 만들어서 넘기는 방법 {Type-Safe 하지 않다.}
      @Parameters({
            "0, 0, true",
            "100, 0, false",
            "0, 100, true"
        })

      방법 2
      메서드의 명을 넘기는 방법
      @Parameters(paramsForTestFree)
      private Object[] paramsForTestFree() {
        return new Object[]{
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 100, false}
        };
    }
      방법 3
      메서드의 네임 컨밴션을 지켜서 만드는 것
      이 경우 @Parameters에 인자를 생략하고 메서드만 만들어서 사용할 수 있다.
      parametersFor....
    */

    @Test
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build()
                ;
        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);

        //given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
    }

    private Object[] parametersForTestFree() {
        return new Object[]{
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 100, false}
        };
    }


    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        //Given
        Event event = Event.builder()
                .location(location)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline() {
        return new Object[]{
                new Object[] {"강남", true},
                new Object[] {null, false},
                new Object[] {" ", false},
        };
    }

}