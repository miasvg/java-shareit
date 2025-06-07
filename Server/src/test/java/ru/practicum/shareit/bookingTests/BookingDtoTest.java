package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingDto dto = new BookingDto(LocalDateTime.of(2025, 1, 1, 12, 0),
                LocalDateTime.of(2025, 1, 2, 12, 0), 1L);

        JsonContent<BookingDto> result = json.write(dto);

        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.itemId");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"start\":\"2025-01-01T12:00:00\",\"end\":\"2025-01-02T12:00:00\",\"itemId\":1}";
        BookingDto result = json.parseObject(content);

        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2025, 1, 2, 12, 0));
        assertThat(result.getItemId()).isEqualTo(1L);
    }
}
