package simple.blog.backend.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import simple.blog.backend.enums.RoomType;

@Getter
@Setter
public class ChatRoomResponse {
    private String chatRoomId;
	 private String roomName;
	 private String roomPicture;
     private List<Integer> userIds;
     private RoomType roomType;
}
