package com.ds.chat.service;

import com.ds.chat.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatServiceTest {

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService();
    }

    @Test
    void testGetAllMessages_initiallyEmpty() {
        List<Message> messages = chatService.getAllMessages();
        
        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    void testAddMessage_singleMessage() {
        Message message = new Message("testUser", "Hello, world!");
        chatService.addMessage(message);
        
        List<Message> messages = chatService.getAllMessages();
        
        assertEquals(1, messages.size());
        assertEquals("testUser", messages.get(0).getUsername());
        assertEquals("Hello, world!", messages.get(0).getContent());
    }

    @Test
    void testAddMessage_multipleMessages() {
        Message message1 = new Message("user1", "First message");
        Message message2 = new Message("user2", "Second message");
        Message message3 = new Message("user1", "Third message");
        
        chatService.addMessage(message1);
        chatService.addMessage(message2);
        chatService.addMessage(message3);
        
        List<Message> messages = chatService.getAllMessages();
        
        assertEquals(3, messages.size());
        assertEquals("First message", messages.get(0).getContent());
        assertEquals("Second message", messages.get(1).getContent());
        assertEquals("Third message", messages.get(2).getContent());
    }

    @Test
    void testGetAllMessages_returnsUnmodifiableList() {
        Message message = new Message("testUser", "Test message");
        chatService.addMessage(message);
        
        List<Message> messages = chatService.getAllMessages();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            messages.add(new Message("anotherUser", "Another message"));
        });
    }
}