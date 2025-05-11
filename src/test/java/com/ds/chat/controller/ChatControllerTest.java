package com.ds.chat.controller;

import com.ds.chat.service.ChatService;
import com.ds.chat.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
@ActiveProfiles("test")
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(classes = ChatService.class)
    private ChatService chatService;

    @MockBean(classes = RoomService.class)
    private RoomService roomService;

    @Test
    void testIndexEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLoginEndpoint() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}