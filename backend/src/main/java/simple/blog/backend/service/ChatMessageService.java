package simple.blog.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import simple.blog.backend.model.ChatMessage;

public interface ChatMessageService {
    List<ChatMessage> findChatMessagesByChatRoom(String chatRoomId, Map<String, String> requestParam);
    List<ChatMessage> findChatMessagesByChatRoomIdAndSenderId(String chatRoomId, Integer senderId, Sort sort);
    ChatMessage saveChatMessage(ChatMessage chatMessage);

}
