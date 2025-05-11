package com.ds.chat.controller;

import com.ds.chat.model.Room;
import com.ds.chat.model.User;
import com.ds.chat.service.ChatService;
import com.ds.chat.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final ChatService chatService;

    @Autowired
    public RoomController(RoomService roomService, ChatService chatService) {
        this.roomService = roomService;
        this.chatService = chatService;
    }

    @GetMapping
    public String listRooms(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("newRoom", new Room());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("displayName", user.getDisplayName());
        return "rooms";
    }

    @GetMapping("/{id}")
    public String joinRoom(@PathVariable Long id, Authentication authentication, Model model) {
        Room room = roomService.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + id));
        
        User user = (User) authentication.getPrincipal();
        
        model.addAttribute("room", room);
        model.addAttribute("messages", chatService.getMessagesByRoomId(id));
        model.addAttribute("username", user.getUsername());
        model.addAttribute("displayName", user.getDisplayName());
        
        return "chat";
    }

    @PostMapping
    public String createRoom(@ModelAttribute Room room, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        room.setCreatedBy(user.getDisplayName());
        Room savedRoom = roomService.createRoom(room);
        return "redirect:/rooms/" + savedRoom.getId();
    }

    @PostMapping("/validate-name")
    @ResponseBody
    public ResponseEntity<?> validateRoomName(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        boolean exists = roomService.roomExistsByName(name);
        return ResponseEntity.ok(Map.of("valid", !exists));
    }
}