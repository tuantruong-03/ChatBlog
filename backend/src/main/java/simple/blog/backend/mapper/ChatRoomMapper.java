package simple.blog.backend.mapper;

import simple.blog.backend.dto.response.ChatRoomResponse;
import simple.blog.backend.model.ChatRoom;

public class ChatRoomMapper {
    public static ChatRoomResponse toResponseFromModel(ChatRoom chatRoom) {
        ChatRoomResponse chatRoomResponse = new ChatRoomResponse();
        chatRoomResponse.setChatRoomId(chatRoom.getChatRoomId());
        chatRoomResponse.setRoomName(chatRoom.getRoomName());
        chatRoomResponse.setUserIds(chatRoom.getUserIds());
        chatRoomResponse.setRoomPicture(chatRoom.getRoomPicture());
        chatRoomResponse.setRoomType(chatRoom.getRoomType());

        return chatRoomResponse;
    }

}
