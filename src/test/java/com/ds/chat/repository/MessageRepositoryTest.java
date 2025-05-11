package com.ds.chat.repository;

import com.ds.chat.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void testSaveMessage() {
        Message message = new Message("testUser", "Test message");
        Message savedMessage = messageRepository.save(message);
        
        assertNotNull(savedMessage.getId());
        assertEquals("testUser", savedMessage.getUsername());
        assertEquals("Test message", savedMessage.getContent());
        assertNotNull(savedMessage.getTimestamp());
    }

    @Test
    void testFindAllByOrderByTimestampAsc() {
        // Create messages with specific timestamps
        Message message1 = new Message("user1", "Message 1");
        message1.setTimestamp(LocalDateTime.now().minusHours(2));
        
        Message message2 = new Message("user2", "Message 2");
        message2.setTimestamp(LocalDateTime.now().minusHours(1));
        
        Message message3 = new Message("user3", "Message 3");
        message3.setTimestamp(LocalDateTime.now());
        
        // Save in reverse order
        messageRepository.save(message3);
        messageRepository.save(message1);
        messageRepository.save(message2);
        
        // Retrieve ordered by timestamp
        List<Message> messages = messageRepository.findAllByOrderByTimestampAsc();
        
        assertEquals(3, messages.size());
        assertEquals("Message 1", messages.get(0).getContent());
        assertEquals("Message 2", messages.get(1).getContent());
        assertEquals("Message 3", messages.get(2).getContent());
    }
}