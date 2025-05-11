package com.ds.chat.controller;

import com.ds.chat.model.Message;
import com.ds.chat.model.Room;
import com.ds.chat.service.ChatService;
import com.ds.chat.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final RoomService roomService;

    @Autowired
    public ChatController(ChatService chatService, RoomService roomService) {
        this.chatService = chatService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public Message sendMessage(@DestinationVariable Long roomId, Message message) {
        return chatService.addMessage(message, roomId);
    }
}