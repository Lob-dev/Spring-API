package com.lob.demoinflearnrestapi.events;


import lombok.*;

import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor // 롬복 어노테이션은 다른 어노테이션과 다르게 메타 어노테이션을 통한 사용이 불가능하다.
@Getter @Setter @EqualsAndHashCode(of = "id") // of id? entity 간의 연관 관계가 있을 때 상호 참조하는 관계가 되어버리면 스택오버플로우가 발생 가능
public class Event {                          // id의 값만 가지고 equals와 hashcode를 비교함 연관 관계에 해당하는 녀석은 넣으면 안된다

    private Integer id; // id 값을 통하여 DB 조회를 수행한다.
    private String name; // 명
    private String description; // 설명
    private LocalDateTime beginEnrollmentDateTime; // 시작 일시
    private LocalDateTime closeEnrollmentDateTime; // 종료 일시
    private LocalDateTime beginEventDateTime; // 이벤트 시작 일시
    private LocalDateTime endEventDateTime; // 이벤트 종료 일시
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus;


}
