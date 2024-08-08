package simple.blog.backend.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.ChatMessageRequest;
import simple.blog.backend.model.ChatMessage;
import simple.blog.backend.model.ChatRoom;
import simple.blog.backend.service.ChatMessageService;
import simple.blog.backend.service.ChatRoomService;
import simple.blog.backend.service.UserService;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    

    @MessageMapping("/messages-queue")
    public ChatMessageRequest distributeMessages(@Payload ChatMessageRequest chatMessageRequest) {
        System.out.println(chatMessageRequest.toString());
        ChatRoom chatRoom = chatRoomService.findChatRoomById(chatMessageRequest.getChatRoomId());
        ChatMessage chatMessage = ChatMessage.builder()
            .chatMessageId(UUID.randomUUID().toString())
            .chatRoomId(chatMessageRequest.getChatRoomId())
            .content(chatMessageRequest.getContent())
            .timestamp(new Date())
            .senderId(chatMessageRequest.getSenderId())
            .build();
        ChatMessage savedChatMessage = chatMessageService.saveChatMessage(chatMessage);
        chatRoomService.addMessageToChatRoom(savedChatMessage.getChatMessageId(), chatRoom);
        List<Integer> userIds = chatRoom.getUserIds();
        List<Integer> otherUserIds = userIds.stream().filter(userId -> !userId.equals(chatMessageRequest.getSenderId())).collect(Collectors.toList());
        for (Integer userId : otherUserIds) {
            simpMessagingTemplate.convertAndSendToUser(userId.toString(), "/private", chatMessageRequest );
        }
        return chatMessageRequest;
    }
}
