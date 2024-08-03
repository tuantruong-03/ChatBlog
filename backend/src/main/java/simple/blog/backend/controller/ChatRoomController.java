package simple.blog.backend.controller;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import simple.blog.backend.dto.request.ChatRoomRequest;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.mapper.UserMapper;
import simple.blog.backend.model.ChatRoom;

@Controller
@RequestMapping("api/v1/chat-room")
public class ChatRoomController {

    @PostMapping("/find-or-create")
    public ResponseEntity<ApiResponse> getOrCreateChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.statusCode(HttpStatus.OK.value())
                .data(null)
    			.build();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
