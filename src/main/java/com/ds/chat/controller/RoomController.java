package com.ds.chat.controller;

import com.ds.chat.model.Room;
import com.ds.chat.service.ChatService;
import com.ds.chat.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public String listRooms(@RequestParam(required = false) String username, Model model) {
        if (username == null || username.trim().isEmpty()) {
            return "redirect:/login";
        }

        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("newRoom", new Room());
        model.addAttribute("username", username);
        return "rooms";
    }

    @GetMapping("/{id}")
    public String joinRoom(@PathVariable Long id, @RequestParam(required = false) String username, Model model) {
        Room room = roomService.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + id));

        model.addAttribute("room", room);
        model.addAttribute("messages", chatService.getMessagesByRoomId(id));

        if (username != null && !username.trim().isEmpty()) {
            model.addAttribute("username", username);
        } else {
            // Redirect to login if username is not provided
            return "redirect:/login";
        }

        return "chat";
    }

    @PostMapping
    public String createRoom(@ModelAttribute Room room, @RequestParam String username) {
        room.setCreatedBy(username);
        Room savedRoom = roomService.createRoom(room);
        return "redirect:/rooms/" + savedRoom.getId() + "?username=" + username;
    }

    @PostMapping("/validate-name")
    @ResponseBody
    public ResponseEntity<?> validateRoomName(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        boolean exists = roomService.roomExistsByName(name);
        return ResponseEntity.ok(Map.of("valid", !exists));
    }
}