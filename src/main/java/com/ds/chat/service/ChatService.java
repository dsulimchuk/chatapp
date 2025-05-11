package com.ds.chat.service;

import com.ds.chat.model.Message;
import com.ds.chat.model.Room;
import com.ds.chat.model.User;
import com.ds.chat.repository.MessageRepository;
import com.ds.chat.repository.RoomRepository;
import com.ds.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatService(MessageRepository messageRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByTimestampAsc();
    }

    @Transactional(readOnly = true)
    public List<Message> getMessagesByRoomId(Long roomId) {
        // Get messages but filter out any with null or empty content
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId).stream()
                .filter(message -> message.getContent() != null && !message.getContent().trim().isEmpty())
                .toList();
    }

    @Transactional
    public Message addMessage(Message message, Long roomId) {
        // Validate message content
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        // Trim message content
        message.setContent(message.getContent().trim());

        // Check for duplicate system join messages (within 5 seconds)
        if ("System".equals(message.getUsername()) && message.getContent().endsWith("joined the chat")) {
            if (isDuplicateJoinMessage(roomId, message.getContent())) {
                // This is a duplicate join message, just return it without saving
                System.out.println("Skipping duplicate system join message: " + message.getContent());
                return message;
            }
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
        message.setRoom(room);

        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is available
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                message.setUser((User) principal);
            } else {
                // Fallback to finding user by username if available
                String username = authentication.getName();
                if (username != null && !username.equals("anonymousUser")) {
                    User user = userRepository.findByUsername(username)
                            .orElseGet(() -> findOrCreateSystemUser());
                    message.setUser(user);
                } else {
                    message.setUser(findOrCreateSystemUser());
                }
            }
        } else {
            // If no authentication, use a system user
            message.setUser(findOrCreateSystemUser());
        }

        return messageRepository.save(message);
    }

    /**
     * Check if this is a duplicate system join message within the last 5 seconds
     */
    private boolean isDuplicateJoinMessage(Long roomId, String content) {
        LocalDateTime fiveSecondsAgo = LocalDateTime.now().minusSeconds(5);
        List<Message> recentMessages = messageRepository.findByRoomIdAndUsernameAndContentAndTimestampAfter(
            roomId, "System", content, fiveSecondsAgo);
        return !recentMessages.isEmpty();
    }

    private User findOrCreateSystemUser() {
        return userRepository.findByUsername("system")
                .orElseGet(() -> {
                    User systemUser = new User("system", "system", "System");
                    return userRepository.save(systemUser);
                });
    }
}