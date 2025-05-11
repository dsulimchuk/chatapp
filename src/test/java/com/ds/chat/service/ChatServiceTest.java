package com.ds.chat.service;

import com.ds.chat.model.Message;
import com.ds.chat.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    void testGetAllMessages_initiallyEmpty() {
        when(messageRepository.findAllByOrderByTimestampAsc()).thenReturn(Collections.emptyList());

        List<Message> messages = chatService.getAllMessages();

        assertNotNull(messages);
        assertTrue(messages.isEmpty());
        verify(messageRepository).findAllByOrderByTimestampAsc();
    }

    @Test
    void testAddMessage_singleMessage() {
        Message message = new Message("testUser", "Hello, world!");
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageRepository.findAllByOrderByTimestampAsc()).thenReturn(Collections.singletonList(message));

        chatService.addMessage(message);
        List<Message> messages = chatService.getAllMessages();

        assertEquals(1, messages.size());
        assertEquals("testUser", messages.get(0).getUsername());
        assertEquals("Hello, world!", messages.get(0).getContent());
        verify(messageRepository).save(message);
        verify(messageRepository).findAllByOrderByTimestampAsc();
    }

    @Test
    void testAddMessage_multipleMessages() {
        Message message1 = new Message("user1", "First message");
        Message message2 = new Message("user2", "Second message");
        Message message3 = new Message("user1", "Third message");

        when(messageRepository.save(message1)).thenReturn(message1);
        when(messageRepository.save(message2)).thenReturn(message2);
        when(messageRepository.save(message3)).thenReturn(message3);

        List<Message> expectedMessages = Arrays.asList(message1, message2, message3);
        when(messageRepository.findAllByOrderByTimestampAsc()).thenReturn(expectedMessages);

        chatService.addMessage(message1);
        chatService.addMessage(message2);
        chatService.addMessage(message3);

        List<Message> messages = chatService.getAllMessages();

        assertEquals(3, messages.size());
        assertEquals("First message", messages.get(0).getContent());
        assertEquals("Second message", messages.get(1).getContent());
        assertEquals("Third message", messages.get(2).getContent());

        verify(messageRepository, times(3)).save(any(Message.class));
        verify(messageRepository).findAllByOrderByTimestampAsc();
    }

    @Test
    void testAddMessage_returnsPersistedMessage() {
        Message input = new Message("testUser", "Test message");
        Message saved = new Message("testUser", "Test message");
        saved.setId(1L);

        when(messageRepository.save(input)).thenReturn(saved);

        Message result = chatService.addMessage(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testUser", result.getUsername());
        assertEquals("Test message", result.getContent());

        verify(messageRepository).save(input);
    }
}