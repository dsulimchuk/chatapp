package com.ds.chat.service;

import com.ds.chat.model.Message;
import com.ds.chat.model.Room;
import com.ds.chat.repository.MessageRepository;
import com.ds.chat.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RoomRepository roomRepository;

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
    void testGetMessagesByRoomId() {
        Room room = new Room("Test Room", "Test Description", "testUser");
        room.setId(1L);
        
        Message message1 = new Message("user1", "Room message 1", room);
        Message message2 = new Message("user2", "Room message 2", room);
        
        when(messageRepository.findByRoomIdOrderByTimestampAsc(1L))
            .thenReturn(Arrays.asList(message1, message2));
        
        List<Message> messages = chatService.getMessagesByRoomId(1L);
        
        assertEquals(2, messages.size());
        assertEquals("Room message 1", messages.get(0).getContent());
        assertEquals("Room message 2", messages.get(1).getContent());
        verify(messageRepository).findByRoomIdOrderByTimestampAsc(1L);
    }

    @Test
    void testAddMessage_withValidRoom() {
        Room room = new Room("Test Room", "Test Description", "testUser");
        room.setId(1L);
        
        Message message = new Message("testUser", "Hello, world!");
        Message savedMessage = new Message("testUser", "Hello, world!", room);
        savedMessage.setId(100L);
        
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);
        
        Message result = chatService.addMessage(message, 1L);
        
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("testUser", result.getUsername());
        assertEquals("Hello, world!", result.getContent());
        assertEquals(room, result.getRoom());
        
        verify(roomRepository).findById(1L);
        verify(messageRepository).save(message);
    }
    
    @Test
    void testAddMessage_withInvalidRoom() {
        Message message = new Message("testUser", "Hello, world!");
        
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> {
            chatService.addMessage(message, 999L);
        });
        
        verify(roomRepository).findById(999L);
        verify(messageRepository, never()).save(any(Message.class));
    }
}