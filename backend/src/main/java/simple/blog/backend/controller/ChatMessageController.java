package simple.blog.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.mapper.ChatMessageMapper;
import simple.blog.backend.model.ChatMessage;
import simple.blog.backend.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/chat-message")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    @GetMapping("{chatRoomId}")
    public ResponseEntity<ApiResponse> getMessagesOfChatRoom(@PathVariable("chatRoomId") String chatRoomId, @RequestParam Map<String, String> requestParam){
        List<ChatMessage> chatMessages = chatMessageService.findChatMessagesByChatRoom(chatRoomId, requestParam);
        ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("Success")
    			.statusCode(HttpStatus.OK.value())
    			.data(chatMessages.stream().map(chatMessage -> ChatMessageMapper.toResponseFromModel(chatMessage)))
    			.build();
        return new ResponseEntity<>(respponse, HttpStatus.OK); 

    }
    
}
