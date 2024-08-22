import React, { useEffect, useRef, useState } from 'react';
import { Button, Card, CardBody, Col, Container, Form, Row, Spinner } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperclip, faSmile, faPaperPlane, faL } from '@fortawesome/free-solid-svg-icons';
import { useAuth } from '../../../hooks/auth-provider';
import useApi from '../../../hooks/api';
import { ref } from 'yup';
import { useChat } from '../../../hooks/chat-provider';

interface MessagesProps {
    chatRoomSelected: any
}

const Messages = (props: MessagesProps) => {
    const { onSortChatRooms } = useChat();
    const { chatRoomSelected } = props;
    const { user, stompClient } = useAuth();
    const [messages, setMessages] = useState<[]>([]) // For display messages list
    const [page, setPage] = useState(0); // Current page for pagination
    const [loading, setLoading] = useState(false); // Loading state for lazy messages loading 
    const api = useApi();
    const messagesEndRef = useRef<HTMLDivElement>(null)
    const messagesContainerRef = useRef<HTMLDivElement>(null);
    const [isUserScrolling, setIsUserScrolling] = useState(false);
    const [otherUsers, setOtherUsers] = useState<Map<number, any>>(new Map());
    const [chatMessageDTO, setChatMessageDTO] = useState({
        chatRoomId: chatRoomSelected.chatRoomId,
        senderId: user.userId,
        content: "",
    }) // For exchange message between client and server

    const fetchMessagesInChatRoomSelected = async () => {
        setLoading(true)
        try {
            const response = await api.get(`/api/v1/chat-message/${chatRoomSelected.chatRoomId}?page=${page}&pageSize=10`)
            const data = response.data.data;
            const messages: [] = data.reverse();
            if (page == 0) {
                setMessages(messages)
            } else {
                setMessages((prevMessages) => [...messages, ...prevMessages])
            }
            // console.log(messages)
        } catch (error) {
            console.error("Failed to fetch messages", error);
        } finally {
            setLoading(false)
        }
    }

    const onMessageReceived = async (payload: any) => {
        console.log(payload.body)
        const payloadData = JSON.parse(payload.body)
        if (payloadData.chatRoomId != chatRoomSelected.chatRoomId) {
            fetchMessagesInChatRoomSelected()
        }
        onSortChatRooms(payloadData);
    }

    useEffect(() => {
        stompClient?.subscribe(`/user/${user.userId}/private`, onMessageReceived)
    }, [])
    // Change chatRoomId when click a user search
    useEffect(() => {
        const userIds = chatRoomSelected.userIds;
        // console.log(userIds)
        const otherUserIds = userIds.filter((userId: number) => userId != user.userId);
        const userMap: Map<number, object> = new Map();
        const fetchOtherUser = async (otherUserIds: []) => {
            for (const otherUserId of otherUserIds) {
                try {
                    const response = await api.get(`/api/v1/users/${otherUserId}`)
                    const data = response.data.data;
                    const { userId, ...userInfo } = data
                    userMap.set(userId, userInfo)
                } catch (error) {
                    console.error("Failed to fetch messages", error);
                }
            }
            setOtherUsers(userMap)
        }
        fetchOtherUser(otherUserIds)

        setChatMessageDTO({ ...chatMessageDTO, chatRoomId: chatRoomSelected.chatRoomId })
    }, [chatRoomSelected])
    // useEffect(() => {
    //     console.log(otherUsers)
    // }, [otherUsers])

    useEffect(() => {
        fetchMessagesInChatRoomSelected();
    }, [chatMessageDTO, page])





    const handleValue = (event: any) => {
        const { value, name } = event.target;
        setChatMessageDTO({ ...chatMessageDTO, [name]: value });
    }

    const handleSendMessage = () => {
        if (chatMessageDTO.content.trim() === "") return
        if (!stompClient) {
            console.log("StompClient is null: ", stompClient)
            return
        }

        stompClient.send('/app/messages-queue', {}, JSON.stringify(chatMessageDTO))
        onSortChatRooms(chatRoomSelected)
        setChatMessageDTO({
            ...chatMessageDTO,
            content: ""
        })
    }

    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => { // For enter
        if (event.key === 'Enter') {
            event.preventDefault(); // Prevent default Enter key behavior (e.g., form submission)
            handleSendMessage();
        }
    };

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    return (
        <>
            <Card className='mb-2'>
                <CardBody style={{ backgroundColor: '#f4f4f4' }} className='d-flex align-items-center'>
                    <div>
                        <img
                            src={chatRoomSelected.roomPicture}
                            alt="avatar" className="d-flex align-self-center me-2 rounded-4" height="50px" width="50px"
                        />
                        {/* <span className={`badge ${user.status === 'ONLINE' ? 'bg-success' : 'bg-secondary'} badge-dot`}></span> */}
                    </div>
                    <div className="pt-1">
                        <p className="fw-bold mb-0 text-decoration-none">{chatRoomSelected.roomName}</p>
                        {/* <p>Inbox</p> */}
                    </div>
                </CardBody>
            </Card>
            <div
                ref={messagesContainerRef}
                className="pt-3 pe-3"
                style={{ height: '400px', overflowY: 'auto' }}>
                {/* List of messages */}
                {messages.length > 0 && (
                    messages.map((message: any, index) => {
                        if (message.senderId === user.userId) {
                            // My message
                            return (
                                <div ref={index == messages.length - 1 ? messagesEndRef : null} key={message.chatMessageId} className="d-flex justify-content-end">
                                    <div className="d-flex flex-column align-items-end">
                                        <p
                                            style={{ maxWidth: '60%', wordBreak: 'break-word', whiteSpace: 'pre-wrap' }}
                                            className="small p-2 me-3 mb-1 text-white rounded-3 bg-primary"
                                        >
                                            {message.content}
                                        </p>
                                        <p className="small me-3 mb-3 rounded-3 text-muted">
                                            {message.timestamp}
                                        </p>
                                    </div>
                                    <img
                                        src={user.profilePicture}
                                        alt="avatar 1"
                                        style={{ width: '45px', height: '45px' }}
                                    />
                                </div>
                            );
                        } else {
                            return (
                                <div ref={index == messages.length - 1 ? messagesEndRef : null} key={message.chatMessageId} className="d-flex justify-content-start">
                                    <img
                                        src={otherUsers.get(message.senderId)?.profilePicture}
                                        alt="avatar 1"
                                        style={{ width: '45px', height: '45px' }}
                                    />
                                    <div className="d-flex flex-column align-items-start">
                                        <p
                                            style={{ backgroundColor: '#f5f6f7', maxWidth: '60%', wordBreak: 'break-word', whiteSpace: 'pre-wrap' }}
                                            className="small p-2 ms-3 mb-1 rounded-3"
                                        >
                                            {message.content}
                                        </p>
                                        <p className="small ms-3 mb-3 rounded-3 text-muted">
                                            {message.timestamp}
                                        </p>
                                    </div>
                                </div>
                            );
                        }
                    })
                )}
                {loading && (
                    <div className="d-flex justify-content-center">
                        <Spinner animation="border" />
                    </div>
                )}
            </div>
            {/* end of List of message */}
            <div className="text-muted d-flex justify-content-start align-items-center pe-3 pt-3 mt-2">
                <img
                    src={user.profilePicture}
                    alt="avatar 3"
                    style={{ width: '50px', height: '50px' }}
                />
                <input
                    name="content"
                    type="text"
                    className="form-control form-control-lg"
                    placeholder="Type message"
                    value={chatMessageDTO.content}
                    onChange={handleValue}
                    // For 'Enter'
                    onKeyDown={handleKeyDown}
                />
                <a className="ms-1 text-muted" href="#!"><FontAwesomeIcon icon={faPaperclip} /></a>
                <a className="ms-3 text-muted" href="#!"><FontAwesomeIcon icon={faSmile} /></a>
                <Button variant="primary" type="button" onClick={handleSendMessage} className="ms-3">
                    <FontAwesomeIcon icon={faPaperPlane} />
                </Button>
            </div>
        </>
    );
};

export default Messages;
