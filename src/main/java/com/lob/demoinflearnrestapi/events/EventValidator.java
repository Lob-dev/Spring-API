package com.lob.demoinflearnrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
//            errors.rejectValue("basePrice", "worongValue", "BasePrice is Wrong");  rejectValue 는 필드에러
//            errors.rejectValue("maxPrice", "worongValue", "MaxPrice is Wrong");
            errors.reject("wrongPrices", "Values fo prices are wrong"); // reject는 글로벌 에러
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())||
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "worongValue", "endEventDateTime is Wrong");
        }

        //TODO beginEventDateTime
        //TODO CloseEnrollmentDateTime
    }
}
