import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useAuth } from './auth-provider';
import useApi from './api';
import { SERVER_BASE_URL } from '../constants/backend-server';

// Define types and interfaces
interface ChatRoom {
    chatRoomId: string;
    roomName: string;
    roomPicture: string;
    // Add other properties as needed
}

interface ChatContextProps {
    chatRooms: ChatRoom[];
    chatRoomSelected: ChatRoom | null;
    setChatRoomSelected: (chatRoom: ChatRoom) => void;
    findOrCreatePrivateChatRoom: (userId: string) => Promise<void>;
    onSortChatRooms: (chatRoom: ChatRoom) => void;
}

interface ChatProviderProps {
    children: ReactNode;
}

const ChatContext = createContext<ChatContextProps | undefined>(undefined);

export const ChatProvider: React.FC<ChatProviderProps> = ({ children }) => {
    const [chatRooms, setChatRooms] = useState<ChatRoom[]>([]);
    const [chatRoomSelected, setChatRoomSelected] = useState<ChatRoom | null>(null);
    const { user } = useAuth();
    const api = useApi();

    // When receive a message from a chatroom
    const onSortChatRooms = async (chatRoom: ChatRoom) => {
        setChatRooms((prevChatRooms) => {
            // Find the chat room with the given chatRoomId
            let chatRoomToMove = prevChatRooms.find(room => room.chatRoomId === chatRoom.chatRoomId);
            if (!chatRoomToMove) chatRoomToMove = chatRoom; // If the room isn't found, return the previous state
    
            // Filter out the room to be moved and place it at the beginning of the array
            const updatedChatRooms = [chatRoomToMove, ...prevChatRooms.filter(room => room.chatRoomId !== chatRoom.chatRoomId)];
            return updatedChatRooms;
        });
    }

    const fetchAllChatRooms = async () => {
        if (!user) return;
        try {
            const response = await api.get(`/api/v1/users/${user.userId}/chatrooms`);
            const chatRooms = response.data.data;
            setChatRooms(chatRooms);
            setChatRoomSelected(chatRooms[0]);
        } catch (err) {
            console.log(err);
        }
    };
    useEffect(() => {
      
        fetchAllChatRooms();
    }, [user]);

    const findOrCreatePrivateChatRoom = async (userId: string) => {
        try {
            const response = await api.post(`${SERVER_BASE_URL}/api/v1/chat-room/find-or-create-private`, { otherUserId: userId });
            const chatroom = response.data.data;
            console.log(chatroom)
            setChatRoomSelected(chatroom);
        } catch (err) {
            console.log(err);
        }
    };

    return (
        <ChatContext.Provider value={{ chatRooms, chatRoomSelected, setChatRoomSelected, findOrCreatePrivateChatRoom, onSortChatRooms }}>
            {children}
        </ChatContext.Provider>
    );
};

export const useChat = (): ChatContextProps => {
    const context = useContext(ChatContext);
    if (context === undefined) {
        throw new Error('useChat must be used within a ChatProvider');
    }
    return context;
};
