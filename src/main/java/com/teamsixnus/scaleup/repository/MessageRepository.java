package com.teamsixnus.scaleup.repository;

import com.teamsixnus.scaleup.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Method to find messages by sender or receiver
    Page<Message> findBySenderProfileIdOrReceiverProfileId(Long senderId, Long receiverId, Pageable pageable);
}
