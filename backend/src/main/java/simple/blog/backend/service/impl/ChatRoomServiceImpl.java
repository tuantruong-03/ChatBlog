package simple.blog.backend.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.PrivateChatRoomRequest;
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
    public ChatRoom findOrCreatePrivateChatRoom(PrivateChatRoomRequest chatRoomRequest) {
        Integer otherUserId = chatRoomRequest.getOtherUserId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        if (!userRepository.existsById(otherUserId)) {
            throw new AppException("User not found with ID " + otherUserId, HttpStatus.NOT_FOUND);
        }
        List<Integer> userIds = new ArrayList<>(Arrays.asList(me.getUserId(), otherUserId));
        System.out.println(userIds.toString());
        // Find private room including me and other user
        ChatRoom chatRoom = chatRoomRepository.findPrivateChatRoom(userIds);
        // System.out.println(chatRoom.toString());
        User otherUser = userRepository.findByUserId(otherUserId); // get otherUser to set roomName and roomPicture
        String firstName = otherUser.getFirstName() != null ? otherUser.getFirstName() : "";
        String lastName = otherUser.getLastName() != null ? otherUser.getLastName() : "";
        if (chatRoom != null) {
            chatRoom.setRoomName(firstName + " " + lastName);
            chatRoom.setRoomName(otherUser.getProfilePicture());
            return chatRoom;
        }
        String chatRoomId = UUID.randomUUID().toString();
        ChatRoom newChatRoom = ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .userIds(userIds)
                .roomName(firstName + " " + lastName)
                .roomPicture(otherUser.getProfilePicture())
                .chatMessageIds(null)
                .roomType(RoomType.PRIVATE)
                .build();

        chatRoomRepository.save(newChatRoom);

        return newChatRoom;
    }

    @Override
    public List<User> findUsersByChatRoom(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new AppException("Chat room not found with ID " + chatRoomId, HttpStatus.NOT_FOUND));
        List<Integer> userIds = chatRoom.getUserIds();
        List<User> users = new ArrayList<>();
        for (Integer userId : userIds) {
            User user = userRepository.findByUserId(userId);
            if (user == null) {
                throw new AppException("User not found with ID " + userId, HttpStatus.NOT_FOUND);
            }
            users.add(user);
        }
        return users;
    }

    private void setRoomName(ChatRoom chatRoom, Integer myUserId) {
        StringJoiner roomName = new StringJoiner(", ");
        for (Integer userId : chatRoom.getUserIds()) {
            if (userId != myUserId) {
                User otherUser = userRepository.findByUserId(userId);
                String firstName = otherUser.getFirstName() != null ? otherUser.getFirstName() : "";
                String lastName = otherUser.getLastName() != null ? otherUser.getLastName() : "";
                roomName.add(firstName + " " + lastName);
                if (chatRoom.getRoomType() == RoomType.PRIVATE) {
                    chatRoom.setRoomPicture(otherUser.getProfilePicture());
                }
            }
        }
        if (roomName.toString().equals("")) {
            return;
        }
        chatRoom.setRoomName(roomName.toString());
    }

    @Override
    public List<ChatRoom> findAllChatRoomsHavingMessages() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        Integer myUserId = me.getUserId();
        chatRooms.forEach(chatRoom -> {
            chatRoom.setRoomName(me.getFirstName() + " " + me.getLastName()); // Case chat to myself
            setRoomName(chatRoom, myUserId); // If not chat to myself, set room name by other users in group chat
        });
        // chat room has at least one message
        return chatRooms.stream().filter(chatRoom ->
        chatRoom.getChatMessageIds().size() > 0).collect(Collectors.toList()); //
        // return chatRooms;
    }

    @Override
    public ChatRoom findChatRoomById(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId);
        if (chatRoom == null) {
            throw new AppException("Chatroom not found with ID " + chatRoomId, HttpStatus.NOT_FOUND);
        }
        return chatRoom;
    }

    @Override
    public List<ChatRoom> findChatRoomsOfUser(Integer userId) {
        boolean isUserExisted = userRepository.existsById(userId);
        if (!isUserExisted) {
            throw new AppException("User not found with ID " + userId, HttpStatus.NOT_FOUND);
        }
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User me = (User) authentication.getPrincipal();
        Integer myUserId = me.getUserId();
        chatRooms.forEach(chatRoom -> {
            chatRoom.setRoomName(me.getFirstName() + " " + me.getLastName()); // Case chat to myself
            setRoomName(chatRoom, myUserId); // If not chat to myself, set room name by other users in group chat
        });
        return chatRooms.stream().filter(chatRoom -> chatRoom.getChatMessageIds()!= null &&
        chatRoom.getChatMessageIds().size() > 0).collect(Collectors.toList()); // ChatRoom have messages
    }

    @Override
    public ChatRoom addMessageToChatRoom(String chatMessageId, ChatRoom chatRoom) {
        if (chatRoom.getChatMessageIds() == null) {
            chatRoom.setChatMessageIds(new ArrayList<>());
        }
        chatRoom.getChatMessageIds().add(chatMessageId);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

}
