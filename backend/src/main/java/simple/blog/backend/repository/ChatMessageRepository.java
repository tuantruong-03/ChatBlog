package simple.blog.backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import simple.blog.backend.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomId(String chatRoomId, Pageable pageable);
    List<ChatMessage> findByChatRoomIdAndSenderId(String chatRoomId, Integer senderId, Sort sort);
}
