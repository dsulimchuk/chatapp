package com.ds.chat.repository;

import com.ds.chat.model.Message;
import com.ds.chat.model.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void testSaveMessage() {
        // Get the test room that's created by data.sql (ID=1)
        Room room = new Room();
        room.setId(1L);

        Message message = new Message("testUser", "Test message", room);
        Message savedMessage = messageRepository.save(message);

        assertNotNull(savedMessage.getId());
        assertEquals("testUser", savedMessage.getUsername());
        assertEquals("Test message", savedMessage.getContent());
        assertNotNull(savedMessage.getTimestamp());
        assertEquals(1L, savedMessage.getRoom().getId());
    }

    @Test
    void testFindAllByOrderByTimestampAsc() {
        // Create a room for the test
        Room room = new Room();
        room.setId(1L);

        // Create messages with specific timestamps
        Message message1 = new Message("user1", "Message 1", room);
        message1.setTimestamp(LocalDateTime.now().minusHours(2));

        Message message2 = new Message("user2", "Message 2", room);
        message2.setTimestamp(LocalDateTime.now().minusHours(1));

        Message message3 = new Message("user3", "Message 3", room);
        message3.setTimestamp(LocalDateTime.now());

        // Save in reverse order
        messageRepository.save(message3);
        messageRepository.save(message1);
        messageRepository.save(message2);

        // Retrieve ordered by timestamp
        List<Message> messages = messageRepository.findAllByOrderByTimestampAsc();

        // Get only the messages we just created (there might be pre-existing ones)
        List<Message> ourMessages = messages.stream()
            .filter(m -> m.getContent().startsWith("Message "))
            .toList();

        assertEquals(3, ourMessages.size());
        assertEquals("Message 1", ourMessages.get(0).getContent());
        assertEquals("Message 2", ourMessages.get(1).getContent());
        assertEquals("Message 3", ourMessages.get(2).getContent());
    }
}