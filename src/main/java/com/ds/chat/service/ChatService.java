package com.ds.chat.service;

import com.ds.chat.model.Message;
import com.ds.chat.model.Room;
import com.ds.chat.repository.MessageRepository;
import com.ds.chat.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {
    
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    
    @Autowired
    public ChatService(MessageRepository messageRepository, RoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByTimestampAsc();
    }
    
    @Transactional(readOnly = true)
    public List<Message> getMessagesByRoomId(Long roomId) {
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
    
    @Transactional
    public Message addMessage(Message message, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
        message.setRoom(room);
        return messageRepository.save(message);
    }
}