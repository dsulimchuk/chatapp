package com.ds.chat.service;

import com.ds.chat.model.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {
    
    private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    
    public List<Message> getAllMessages() {
        return Collections.unmodifiableList(messages);
    }
    
    public void addMessage(Message message) {
        messages.add(message);
    }
}