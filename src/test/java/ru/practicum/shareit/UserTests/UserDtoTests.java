package ru.practicum.shareit.UserTests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@JsonTest
public class UserDtoTests {

    @Autowired
    private JacksonTester<UserDto> json;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
   public void serialize() throws Exception {
        UserDto dto = new UserDto(1L, "Alice", "alice@example.com");
        JsonContent<UserDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("alice@example.com");
    }

    @Test
    public void validation_OnCreate_EmailIsMandatory() {
        UserDto dto = new UserDto(null, "Alice", null);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, UserDto.OnCreate.class);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Email не может быть пустым");
    }
}
