package com.ds.chat.repository;

import com.ds.chat.model.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testFindByName() {
        // Test room is inserted by data.sql
        Optional<Room> room = roomRepository.findByName("Test Room");
        assertTrue(room.isPresent());
        assertEquals("Test Room", room.get().getName());
        assertEquals("A room for testing", room.get().getDescription());
        assertEquals("System", room.get().getCreatedBy());
    }

    @Test
    void testSaveRoom() {
        Room room = new Room("New Room", "New Description", "testUser");
        Room savedRoom = roomRepository.save(room);
        
        assertNotNull(savedRoom.getId());
        assertEquals("New Room", savedRoom.getName());
        assertEquals("New Description", savedRoom.getDescription());
        assertEquals("testUser", savedRoom.getCreatedBy());
    }

    @Test
    void testExistsByName() {
        // Test room is inserted by data.sql
        assertTrue(roomRepository.existsByName("Test Room"));
        assertFalse(roomRepository.existsByName("Non-existent Room"));
    }
}