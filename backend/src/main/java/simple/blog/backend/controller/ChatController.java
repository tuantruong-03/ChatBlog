package simple.blog.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.model.ChatMessage;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    

    // Client will send "stomp" to "/app/private-message"
    @MessageMapping("/messages-queue")
    public ChatMessage distributeMessages(@Payload ChatMessage message) {
        return null;
    }
}
