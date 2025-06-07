package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> createDtoTester;

    @Autowired
    private JacksonTester<ItemRequestResponseDto> responseDtoTester;

    @Test
    public void testSerializeAndDeserializeItemRequestCreateDto() throws Exception {
        ItemRequestCreateDto dto = new ItemRequestCreateDto("Нужен молоток", LocalDateTime.of(2025, 6, 1, 12, 0));
        String json = createDtoTester.write(dto).getJson();

        ItemRequestCreateDto parsed = createDtoTester.parse(json).getObject();
        assertEquals(dto.getDescription(), parsed.getDescription());
        assertEquals(dto.getCreated(), parsed.getCreated());
    }

    @Test
    public void testSerializeItemRequestResponseDto() throws Exception {
        ItemRequestResponseDto dto = ItemRequestResponseDto.builder()
                .id(1L)
                .description("Описание")
                .created(LocalDateTime.of(2025, 6, 1, 12, 0))
                .items(List.of())
                .build();

        String json = responseDtoTester.write(dto).getJson();
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"description\":\"Описание\""));
    }
}
