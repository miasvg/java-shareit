package ru.practicum.shareit.bookingTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
class BookingDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialization() throws Exception {
        BookingDto dto = new BookingDto(
                LocalDateTime.of(2030, 1, 1, 12, 0),
                LocalDateTime.of(2030, 1, 2, 12, 0),
                1L
        );

        String json = objectMapper.writeValueAsString(dto);
        assertThat(json).contains("2030-01-01T12:00", "2030-01-02T12:00", "\"itemId\":1");
    }

    @Test
    void testInvalidDto_shouldFailValidation() {
        BookingDto dto = new BookingDto(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1),
                null
        );

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertThat(violations).hasSizeGreaterThan(0);
    }
}

