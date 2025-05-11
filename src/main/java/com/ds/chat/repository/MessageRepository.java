package com.ds.chat.repository;

import com.ds.chat.model.Message;
import com.ds.chat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByTimestampAsc();
    List<Message> findByRoomOrderByTimestampAsc(Room room);

    // Use Room object instead of roomId since roomId is a transient property
    @Query("SELECT m FROM Message m WHERE m.room.id = :roomId ORDER BY m.timestamp ASC")
    List<Message> findByRoomIdOrderByTimestampAsc(@Param("roomId") Long roomId);
}