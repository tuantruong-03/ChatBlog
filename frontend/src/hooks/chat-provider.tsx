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
    onChangeChatRooms: () => Promise<void>;
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

    const onChangeChatRooms = async () => {
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
      
        onChangeChatRooms();
    }, [user]);

    const findOrCreatePrivateChatRoom = async (userId: string) => {
        try {
            const response = await api.post(`${SERVER_BASE_URL}/api/v1/chat-room/find-or-create-private`, { otherUserId: userId });
            const chatroom = response.data.data;
            setChatRoomSelected(chatroom);
        } catch (err) {
            console.log(err);
        }
    };

    return (
        <ChatContext.Provider value={{ chatRooms, chatRoomSelected, setChatRoomSelected, findOrCreatePrivateChatRoom, onChangeChatRooms }}>
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
