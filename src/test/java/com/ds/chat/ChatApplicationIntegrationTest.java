package com.ds.chat;

import com.ds.chat.model.Message;
import com.ds.chat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ChatApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatService chatService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Verifies that the application context loads successfully
    }

    @Test
    void testHomePageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("chat"))
                .andExpect(model().attributeExists("messages"));
    }

    @Test
    void testChatServiceIntegration() {
        // Given
        chatService.addMessage(new Message("testUser", "Integration Test Message"));
        
        // When
        String response = restTemplate.getForObject("http://localhost:" + port + "/", String.class);
        
        // Then
        assertTrue(response.contains("testUser"));
        assertTrue(response.contains("Integration Test Message"));
    }

    @Test
    void testAddAndRetrieveMessages() {
        // Clear previous test messages if any
        // Note: In a real application, you'd want a way to reset the state between tests
        
        // Given
        int initialCount = chatService.getAllMessages().size();
        Message message1 = new Message("user1", "First integration message");
        Message message2 = new Message("user2", "Second integration message");
        
        // When
        chatService.addMessage(message1);
        chatService.addMessage(message2);
        
        // Then
        assertEquals(initialCount + 2, chatService.getAllMessages().size());
        assertEquals("First integration message", chatService.getAllMessages().get(initialCount).getContent());
        assertEquals("Second integration message", chatService.getAllMessages().get(initialCount + 1).getContent());
    }
}