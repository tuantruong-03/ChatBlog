package simple.blog.backend.service;

import java.util.List;

import simple.blog.backend.dto.request.PrivateChatRoomRequest;
import simple.blog.backend.model.ChatRoom;
import simple.blog.backend.model.User;

public interface ChatRoomService {
	ChatRoom findOrCreatePrivateChatRoom(PrivateChatRoomRequest chatRoomRequest);
	List<User> findUsersByChatRoom(String chatRoomId);
	List<ChatRoom> findAllChatRoomsHavingMessages();
	ChatRoom findChatRoomById(String chatRoomId);
	List<ChatRoom> findChatRoomsOfUser(Integer userId);
	ChatRoom addMessageToChatRoom(String chatMessageId, ChatRoom chatRoom);
}
