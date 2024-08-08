package simple.blog.backend.dto.response;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageResponse {
    private String chatMessageId;
	private Integer senderId;
	private String content;
	private String timestamp; 
}
