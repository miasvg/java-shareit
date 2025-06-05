package ru.practicum.shareit.itemTests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemCreateDto> json;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void serialize_CreateDto() throws Exception {
        ItemCreateDto dto = new ItemCreateDto("Дрель", "Аккумуляторная", true, null);
        JsonContent<ItemCreateDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
    }

    @Test
    void validate_CreateDto_BlankName() {
        ItemCreateDto dto = new ItemCreateDto("", "Описание", true, null);
        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(dto);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Название не может быть пустым");
    }
}
