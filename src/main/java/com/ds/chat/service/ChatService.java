package com.ds.chat.service;

import com.ds.chat.model.Message;
import com.ds.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {

    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByTimestampAsc();
    }

    @Transactional
    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }
}