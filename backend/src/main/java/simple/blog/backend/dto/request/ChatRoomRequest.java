package simple.blog.backend.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import simple.blog.backend.enums.RoomType;
import simple.blog.backend.validation.EnumValue;

public class ChatRoomRequest {
    @NotNull(message = "userIds must not be null")
    @NotBlank(message = "userIds must not be empty")
    List<Integer> userIds;

    @EnumValue(name = "roomType", enumClass = RoomType.class)
    @NotNull
    RoomType roomType;
}
