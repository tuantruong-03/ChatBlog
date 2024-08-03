package simple.blog.backend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.enums.RoomType;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.model.ChatRoom;
import simple.blog.backend.model.User;
import simple.blog.backend.repository.ChatRoomRepository;
import simple.blog.backend.repository.UserRepository;
import simple.blog.backend.service.ChatRoomService;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public ChatRoom findOrCreatePrivateChatRoom(List<Integer> userIds) { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User)authentication.getPrincipal();
        boolean isMyIdContained = false;
        Integer otherUserId = null;
        for (Integer userId : userIds) {
            if (!userRepository.existsById(userId)) {
                throw new AppException("User not found with ID " + userId, HttpStatus.NOT_FOUND);
            }
            if (userId != me.getUserId()) {
                otherUserId = userId;
            }
            if (userId == me.getUserId()) {
                isMyIdContained = true;
            }
        }
        if (!isMyIdContained) {
            throw new AppException("Not contain userId of client", HttpStatus.NOT_FOUND);
        }
        User otherUser = userRepository.findByUserId(otherUserId); // get otherUser to set roomName and roomPicture

        ChatRoom room = chatRoomRepository.findPrivateChatRoom(userIds);
        if (room != null) {
            return room;
        }

        String firstName = otherUser.getFirstName() == null ? otherUser.getFirstName() : "";
        String lastName = otherUser.getLastName() == null ? otherUser.getLastName() : "";
        String chatRoomId = UUID.randomUUID().toString();
        ChatRoom newChatRoom = ChatRoom.builder()
        						.chatRoomId(chatRoomId)
        						.userIds(userIds)
                                .roomName(firstName + lastName)
                                .roomPicture(otherUser.getProfilePicture())
        						.chatMessageIds(null)
                                .roomType(RoomType.PRIVATE)
        						.build();

        chatRoomRepository.save(newChatRoom);

        return newChatRoom;
    }

    @Override
    public List<User> findUsersByChatRoom(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new AppException("Chat room not found with ID " + chatRoomId, HttpStatus.NOT_FOUND));
        List<Integer> userIds = chatRoom.getUserIds();
        List<User> users = new ArrayList<>();
        for (Integer userId : userIds) {
            User user=  userRepository.findByUserId(userId);
            if (user == null) {
                throw new AppException("User not found with ID " + userId, HttpStatus.NOT_FOUND);
            }
            users.add(user);   
        }
        return users;
    }

}

