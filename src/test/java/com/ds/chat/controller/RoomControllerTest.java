package com.ds.chat.controller;

import com.ds.chat.model.Message;
import com.ds.chat.model.Room;
import com.ds.chat.service.ChatService;
import com.ds.chat.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
@ActiveProfiles("test")
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(classes = RoomService.class)
    private RoomService roomService;

    @MockBean(classes = ChatService.class)
    private ChatService chatService;

    @Test
    void testListRooms() throws Exception {
        // Given
        Room room1 = new Room("Room 1", "Description 1", "user1");
        Room room2 = new Room("Room 2", "Description 2", "user2");
        
        when(roomService.getAllRooms()).thenReturn(Arrays.asList(room1, room2));

        // When & Then
        mockMvc.perform(get("/rooms").param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(model().attributeExists("newRoom"));
    }

    @Test
    void testJoinRoom() throws Exception {
        // Given
        Room room = new Room("Test Room", "Test Description", "testUser");
        room.setId(1L);
        
        Message message1 = new Message("user1", "Hello");
        message1.setTimestamp(LocalDateTime.now());
        
        when(roomService.getRoomById(1L)).thenReturn(Optional.of(room));
        when(chatService.getMessagesByRoomId(1L)).thenReturn(Collections.singletonList(message1));

        // When & Then
        mockMvc.perform(get("/rooms/1").param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("chat"))
                .andExpect(model().attributeExists("room"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attribute("username", "testUser"));
    }

    @Test
    void testCreateRoom() throws Exception {
        // Given
        Room newRoom = new Room("New Room", "New Description", "testUser");
        newRoom.setId(1L);
        
        when(roomService.createRoom(any(Room.class))).thenReturn(newRoom);

        // When & Then
        mockMvc.perform(post("/rooms")
                    .param("name", "New Room")
                    .param("description", "New Description")
                    .param("username", "testUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/rooms/1?username=testUser"));
    }

    @Test
    void testValidateRoomName_valid() throws Exception {
        // Given
        when(roomService.roomExistsByName(anyString())).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/rooms/validate-name")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"New Room\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    void testValidateRoomName_invalid() throws Exception {
        // Given
        when(roomService.roomExistsByName(anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/rooms/validate-name")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Existing Room\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }
}