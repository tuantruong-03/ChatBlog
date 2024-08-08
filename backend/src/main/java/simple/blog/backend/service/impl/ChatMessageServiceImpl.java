package simple.blog.backend.service.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.model.ChatMessage;
import simple.blog.backend.repository.ChatMessageRepository;
import simple.blog.backend.repository.ChatRoomRepository;
import simple.blog.backend.service.ChatMessageService;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    @Override
    public List<ChatMessage> findChatMessagesByChatRoom(String chatRoomId) {
        boolean isChatRoomExisted = chatRoomRepository.existsById(chatRoomId);
        if (!isChatRoomExisted) {
            throw new AppException("Chat room not found with ID " + chatRoomId, HttpStatus.NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "timestamp"));
        return chatMessageRepository.findByChatRoomId(chatRoomId, pageable);
    }
    @Override
    public List<ChatMessage> findChatMessagesByChatRoomIdAndSenderId(String chatRoomId, Integer senderId, Sort sort) {
        // TODO Auto-generated method stub
        return chatMessageRepository.findByChatRoomIdAndSenderId(chatRoomId, senderId, Sort.by(Sort.Order.by("timestamp")));
    }
    @Override
    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

}
