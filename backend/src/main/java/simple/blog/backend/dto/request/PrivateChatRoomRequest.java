package simple.blog.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class PrivateChatRoomRequest {
    @NotNull(message = "otherUserId must not be null")
    Integer otherUserId;
}
