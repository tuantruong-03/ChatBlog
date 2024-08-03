package simple.blog.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import simple.blog.backend.enums.RoomType;
import simple.blog.backend.model.ChatRoom;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    ChatRoom findByChatRoomId(String chatRoomId);
    
    @Query("{ 'userIds': { $all: ?0 }, 'roomType': 'PRIVATE' }")
    ChatRoom findPrivateChatRoom(List<Integer> userIds); // "userIds" only contains two users
}
