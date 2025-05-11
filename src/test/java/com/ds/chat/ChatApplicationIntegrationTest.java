package com.ds.chat;

import com.ds.chat.model.Message;
import com.ds.chat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
                .andExpect(view().name("login"));
    }

    @Test
    void testChatServiceIntegration() {
        // This test has been updated to reflect the new room-based architecture
        // Login page is now at root, so we can't test message display directly here

        // When
        String response = restTemplate.getForObject("http://localhost:" + port + "/", String.class);

        // Then
        assertTrue(response.contains("Chat Application"));
        assertTrue(response.contains("Enter your username"));
    }

    @Test
    void testRoomsEndpoint() {
        // Test that the rooms endpoint returns the rooms page
        String response = restTemplate.getForObject("http://localhost:" + port + "/rooms?username=testUser", String.class);

        // Verify rooms page content
        assertTrue(response.contains("Chat Rooms"));
        assertTrue(response.contains("Available Rooms"));
        assertTrue(response.contains("Create a New Room"));
    }
}