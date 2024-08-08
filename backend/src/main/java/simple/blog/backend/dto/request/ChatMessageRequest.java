package simple.blog.backend.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageRequest {
    private String chatRoomId;
    private Integer senderId;
    private String content;

}
