package com.lob.demoinflearnrestapi.events;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {

    // 입력 값이 이상한 경우 (없는 경우)
    // 이것도 Bad_Request를 보내줘야 한다. 혹은 프론트에서 검증해서 수정해야한다.
    // 이 프로젝트에선 @Vaild와 BindingResult  (또는 Erros)
    // @Vaild를 사용하면 해당 어노테이션이 적용된 인스턴스의 값을 바인딩할 때에  @NotNUll, @NotEmpty, @Min, @Max들을 통하여(저장시) 검증을 수행할 수 있다.

    @NotEmpty
    private String name; // 명
    @NotEmpty
    private String description; // 설명
    @NotNull
    private LocalDateTime beginEnrollmentDateTime; // 시작 일시
    @NotNull
    private LocalDateTime closeEnrollmentDateTime; // 종료 일시
    @NotNull
    private LocalDateTime beginEventDateTime; // 이벤트 시작 일시
    @NotNull
    private LocalDateTime endEventDateTime; // 이벤트 종료 일시

    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; // (optional)
    @Min(0)
    private int maxPrice; // (optional)
    @Min(0)
    private int limitOfEnrollment;



}
