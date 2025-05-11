package com.ds.chat.service;

import com.ds.chat.model.Room;
import com.ds.chat.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Room> getRoomByName(String name) {
        return roomRepository.findByName(name);
    }

    @Transactional
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public boolean roomExistsByName(String name) {
        return roomRepository.existsByName(name);
    }
}