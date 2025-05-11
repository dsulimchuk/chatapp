package com.ds.chat.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testDefaultConstructor() {
        Message message = new Message();
        
        assertNull(message.getUsername());
        assertNull(message.getContent());
        assertNotNull(message.getTimestamp());
        
        LocalDateTime now = LocalDateTime.now();
        long secondsDifference = Math.abs(ChronoUnit.SECONDS.between(now, message.getTimestamp()));
        
        // The timestamp should be very close to the current time
        assertTrue(secondsDifference < 2, "Timestamp should be initialized to current time");
    }

    @Test
    void testParameterizedConstructor() {
        Message message = new Message("testUser", "Test message");
        
        assertEquals("testUser", message.getUsername());
        assertEquals("Test message", message.getContent());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void testSetters() {
        Message message = new Message();
        LocalDateTime customTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        
        message.setUsername("updatedUser");
        message.setContent("Updated content");
        message.setTimestamp(customTime);
        
        assertEquals("updatedUser", message.getUsername());
        assertEquals("Updated content", message.getContent());
        assertEquals(customTime, message.getTimestamp());
    }
}