package simple.blog.backend.service;

import java.util.List;

import simple.blog.backend.model.ChatRoom;
import simple.blog.backend.model.User;

public interface ChatRoomService {
	ChatRoom findOrCreatePrivateChatRoom(List<Integer> userIds);
	List<User> findUsersByChatRoom(String chatRoomId);
}
