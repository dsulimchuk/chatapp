package com.ds.chat.service;

import com.ds.chat.model.Room;
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
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void testGetAllRooms() {
        Room room1 = new Room("Room 1", "Description 1", "user1");
        Room room2 = new Room("Room 2", "Description 2", "user2");
        
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));
        
        List<Room> rooms = roomService.getAllRooms();
        
        assertEquals(2, rooms.size());
        assertEquals("Room 1", rooms.get(0).getName());
        assertEquals("Room 2", rooms.get(1).getName());
        verify(roomRepository).findAll();
    }

    @Test
    void testGetAllRooms_empty() {
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<Room> rooms = roomService.getAllRooms();
        
        assertTrue(rooms.isEmpty());
        verify(roomRepository).findAll();
    }

    @Test
    void testGetRoomById_existingRoom() {
        Room room = new Room("Test Room", "Test Description", "testUser");
        room.setId(1L);
        
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        
        Optional<Room> result = roomService.getRoomById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("Test Room", result.get().getName());
        verify(roomRepository).findById(1L);
    }

    @Test
    void testGetRoomById_nonExistingRoom() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<Room> result = roomService.getRoomById(999L);
        
        assertFalse(result.isPresent());
        verify(roomRepository).findById(999L);
    }

    @Test
    void testGetRoomByName_existingRoom() {
        Room room = new Room("Test Room", "Test Description", "testUser");
        
        when(roomRepository.findByName("Test Room")).thenReturn(Optional.of(room));
        
        Optional<Room> result = roomService.getRoomByName("Test Room");
        
        assertTrue(result.isPresent());
        assertEquals("Test Room", result.get().getName());
        verify(roomRepository).findByName("Test Room");
    }

    @Test
    void testCreateRoom() {
        Room room = new Room("New Room", "New Description", "testUser");
        Room savedRoom = new Room("New Room", "New Description", "testUser");
        savedRoom.setId(1L);
        
        when(roomRepository.save(room)).thenReturn(savedRoom);
        
        Room result = roomService.createRoom(room);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Room", result.getName());
        verify(roomRepository).save(room);
    }

    @Test
    void testRoomExistsByName_existing() {
        when(roomRepository.existsByName("Existing Room")).thenReturn(true);
        
        boolean result = roomService.roomExistsByName("Existing Room");
        
        assertTrue(result);
        verify(roomRepository).existsByName("Existing Room");
    }

    @Test
    void testRoomExistsByName_nonExisting() {
        when(roomRepository.existsByName("Non-Existing Room")).thenReturn(false);
        
        boolean result = roomService.roomExistsByName("Non-Existing Room");
        
        assertFalse(result);
        verify(roomRepository).existsByName("Non-Existing Room");
    }
}