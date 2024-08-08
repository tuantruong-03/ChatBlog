import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Card, Form, Button, ListGroup } from 'react-bootstrap';
import { faPaperclip, faPaperPlane, faSmile } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import UserSearch from './search';
import ChatRoom from './chatroom';
import Messages from './messages';
import { useChat } from '../../../hooks/chat-provider';
import useApi from '../../../hooks/api';

const UserChatLayout = () => {
    const { chatRooms, chatRoomSelected, setChatRoomSelected, findOrCreatePrivateChatRoom } = useChat();
    const [search, setSearch] = useState('');
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const api = useApi();

    const handleSearchChange = (e: any) => {
        setSearch(e.target.value);
    };

    useEffect(() => {
        const fetchUsers = async () => {
            setLoading(true);
            if (!search) {
                setFilteredUsers([]);
                setLoading(false);
                return;
            }
            try {
                const response = await api.get(`/api/v1/users/search`, { params: { query: search } });
                setFilteredUsers(response.data.data);
            } catch (error) {
                console.error('Error fetching users:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchUsers();
    }, [search]);

    return (
        <>
            <section>
                <Row>
                    <Col md={12}>
                        <Card id="chat3" style={{ borderRadius: '15px' }}>
                            <Card.Body>
                                <Row>
                                    <Col md={6} lg={5} xl={4} className="mb-4 mb-md-0 border-end">
                                        <div className="p-3">
                                            <div className="input-group rounded mb-3">
                                                <Form.Control type="search" placeholder="Search" aria-label="Search" aria-describedby="search-addon" className="rounded" value={search} onChange={handleSearchChange} />
                                                <span className="input-group-text border-0" id="search-addon">
                                                    <i className="fas fa-search"></i>
                                                </span>
                                            </div>
                                            <div style={{ position: 'relative', height: '400px', overflowY: 'auto' }}>
                                                {search.length > 0 ? (
                                                    <div className="border rounded p-3" style={{ height: '400px', overflowY: 'auto' }}>
                                                        <ul className="list-unstyled mb-0">
                                                            {loading ? (
                                                                <li key={-1} className="p-2 text-center">Loading...</li>
                                                            ) : filteredUsers.length > 0 ? (
                                                                filteredUsers.map((user: any) => (
                                                                    <ListGroup.Item key={user.userId} onClick={() => findOrCreatePrivateChatRoom(user.userId)} className="p-2 border-bottom" style={{ cursor: 'pointer' }}>
                                                                        <UserSearch user={user} />
                                                                    </ListGroup.Item>
                                                                ))
                                                            ) : (
                                                                <li key={-2} className="p-2 text-center">No users found</li>
                                                            )}
                                                        </ul>
                                                    </div>
                                                ) : (
                                                    <>
                                                        {chatRooms.length > 0 ? (
                                                            chatRooms.map((chatRoom: any) => (
                                                                <ListGroup.Item onClick={() => setChatRoomSelected(chatRoom)} key={chatRoom.chatRoomId} className="p-2 border-bottom list-unstyled" style={{ cursor: 'pointer' }}>
                                                                    <ChatRoom data={chatRoom} />
                                                                </ListGroup.Item>
                                                            ))
                                                        ) : (
                                                            <>No chatroom</>
                                                        )}
                                                    </>
                                                )}
                                            </div>
                                        </div>
                                    </Col>
                                    <Col md={6} lg={7} xl={8}>
                                        {chatRoomSelected && <Messages chatRoomSelected={chatRoomSelected}  />}
                                    </Col>
                                </Row>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </section>
        </>
    );
};

export default UserChatLayout;
