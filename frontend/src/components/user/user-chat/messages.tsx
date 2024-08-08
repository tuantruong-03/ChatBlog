import React, { useEffect, useRef, useState } from 'react';
import { Button, Card, CardBody, Col, Container, Form, Row, Spinner } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperclip, faSmile, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { useAuth } from '../../../hooks/auth-provider';
import useApi from '../../../hooks/api';
import { ref } from 'yup';

interface MessagesProps {
    chatRoomSelected: any
}

const Messages = (props: MessagesProps) => {
    const { chatRoomSelected } = props;
    const { user, stompClient } = useAuth();
    const [messages, setMessages] = useState<[]>([]) // For display messages list
    const api = useApi();
    const messagesEndRef = useRef<HTMLDivElement>(null)
    const [chatMessageDTO, setChatMessageDTO] = useState({
        chatRoomId: chatRoomSelected.chatRoomId,
        senderId: user.userId,
        content: "",
    }) // For exchange message between client and server

    const fetchMessages = async () => {
        try {
            const response = await api.get("/api/v1/chat-message/" + chatRoomSelected.chatRoomId)
            const data = response.data.data;
            const messages = data.reverse()
            setMessages(messages)
            // console.log(messages)
        } catch (error) {
            console.error("Failed to fetch messages", error);
        }
    }

    const onMessageReceived = async (payload: any) => {
        fetchMessages()
    }

    useEffect(() => {
        stompClient?.subscribe(`/user/${user.userId}/private`, onMessageReceived)
    }, [stompClient, user.userId])
    // Change chatRoomId when click a user search
    useEffect(() => {
        setChatMessageDTO({ ...chatMessageDTO, chatRoomId: chatRoomSelected.chatRoomId })
    }, [chatRoomSelected])

    useEffect(() => {
        fetchMessages();
    }, [chatMessageDTO])

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
                <CardBody style={{backgroundColor: '#f4f4f4'}} className='d-flex align-items-center'>
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
                className="pt-3 pe-3"
                style={{ height: '400px', overflowY: 'auto' }}>
                {/* List of messages */}
                {messages.length > 0 ? (
                    messages.map((message: any, index) => {
                        if (message.senderId === user.userId) {
                            // My message
                            return (
                                <div ref={index == messages.length -1 ? messagesEndRef : null} key={message.chatMessageId} className="d-flex justify-content-end">
                                    <div className="d-flex flex-column align-items-end">
                                        <p
                                            style={{ maxWidth: '50%', wordBreak: 'break-word', whiteSpace: 'pre-wrap' }}
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
                                <div ref={index == messages.length -1 ? messagesEndRef : null} key={message.chatMessageId} className="d-flex justify-content-start">
                                    <img
                                        src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava6-bg.webp"
                                        alt="avatar 1"
                                        style={{ width: '45px', height: '45px' }}
                                    />
                                    <div className="d-flex flex-column align-items-start">
                                        <p
                                            style={{ backgroundColor: '#f5f6f7', maxWidth: '50%', wordBreak: 'break-word', whiteSpace: 'pre-wrap' }}
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
                ) : (
                    <>No messages</>
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
