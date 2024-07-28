package simple.blog.backend.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.model.ChatMessage;
import simple.blog.backend.repository.ChatMessageRepository;
import simple.blog.backend.service.ChatMessageService;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    @Override
    public List<ChatMessage> findChatMessagesByChatRoom(String chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId, Sort.by(Sort.Order.by("timestamp")));
    }
    @Override
    public List<ChatMessage> findChatMessagesByChatRoomIdAndSenderId(String chatRoomId, Integer senderId, Sort sort) {
        // TODO Auto-generated method stub
        return chatMessageRepository.findByChatRoomIdAndSenderId(chatRoomId, senderId, Sort.by(Sort.Order.by("timestamp")));
    }

}
