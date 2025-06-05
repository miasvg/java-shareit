package ru.practicum.shareit.requestTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.RequestShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRequest_shouldReturnShortDto() throws Exception {
        ItemRequestCreateDto createDto = new ItemRequestCreateDto("Нужна отвертка", LocalDateTime.now());
        RequestShortDto responseDto = new RequestShortDto(1L, "Нужна отвертка", LocalDateTime.now());

        when(requestService.createRequest(eq(1L), any())).thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserRequests_shouldReturnList() throws Exception {
        List<ItemRequestResponseDto> list = List.of(
                ItemRequestResponseDto.builder().id(1L).description("A").created(LocalDateTime.now()).items(List.of()).build()
        );

        when(requestService.getUserRequests(1L)).thenReturn(list);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("A"));
    }

    @Test
    void getRequestById_shouldReturnDto() throws Exception {
        ItemRequestResponseDto dto = ItemRequestResponseDto.builder()
                .id(1L).description("B").created(LocalDateTime.now()).items(List.of()).build();

        when(requestService.getRequestById(1L)).thenReturn(dto);

        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("B"));
    }

    @Test
    void getAllRequests_shouldReturnList() throws Exception {
        List<ItemRequestResponseDto> list = List.of(
                ItemRequestResponseDto.builder().id(2L).description("C").created(LocalDateTime.now()).items(List.of()).build()
        );

        when(requestService.getAllRequests()).thenReturn(list);

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("C"));
    }
}

