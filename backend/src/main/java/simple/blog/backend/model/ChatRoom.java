package simple.blog.backend.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import simple.blog.backend.enums.RoomType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatRoom {
	 @Id
	 private String chatRoomId;
	 private String roomName;
	 private String roomPicture;
	 private List<Integer> userIds;
	 private List<String> chatMessageIds;
	 RoomType roomType;

}
