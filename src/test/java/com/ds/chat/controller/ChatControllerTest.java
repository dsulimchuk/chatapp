package com.ds.chat.controller;

import com.ds.chat.model.Message;
import com.ds.chat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(classes = ChatService.class)
    private ChatService chatService;

    @Test
    void testChatEndpoint() throws Exception {
        // Given
        Message message1 = new Message("user1", "Hello");
        message1.setTimestamp(LocalDateTime.now());
        
        Message message2 = new Message("user2", "Hi there!");
        message2.setTimestamp(LocalDateTime.now());
        
        List<Message> messages = Arrays.asList(message1, message2);
        
        when(chatService.getAllMessages()).thenReturn(messages);

        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("chat"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attribute("messages", messages));
    }
}