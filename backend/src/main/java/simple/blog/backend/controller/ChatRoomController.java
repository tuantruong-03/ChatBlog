package simple.blog.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.PrivateChatRoomRequest;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.dto.response.ChatRoomResponse;
import simple.blog.backend.mapper.ChatRoomMapper;
import simple.blog.backend.model.ChatRoom;
import simple.blog.backend.service.ChatRoomService;

@Controller
@RequestMapping("/api/v1/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping 
    public ResponseEntity<ApiResponse> getAll() {
        List<ChatRoom> chatRooms = chatRoomService.findAllChatRoomsHavingMessages(); 
        List<ChatRoomResponse> chatRoomResponses = chatRooms.stream().map(chatRoom -> ChatRoomMapper.toResponseFromModel(chatRoom)).collect(Collectors.toList());
        ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.statusCode(HttpStatus.OK.value())
                .data(chatRoomResponses)
    			.build();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/find-or-create-private")
    public ResponseEntity<ApiResponse> getOrCreateChatRoom(@Valid @RequestBody PrivateChatRoomRequest chatRoomRequest) {
        ChatRoom privateChatRoom = chatRoomService.findOrCreatePrivateChatRoom(chatRoomRequest);
        ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.statusCode(HttpStatus.OK.value())
                .data(ChatRoomMapper.toResponseFromModel(privateChatRoom))
    			.build();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
