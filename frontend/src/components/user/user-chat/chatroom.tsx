import { DEFAULT_AVA_URL } from "../../../constants/app";

interface ChatRoomProps {
    data: any;
}
const ChatRoom = (props: ChatRoomProps) => {
    const {data } = props;
    return (
        <>
            <div className="d-flex justify-content-between">
                <div className="d-flex flex-row">
                    <div>
                        <img
                            src={data.roomPicture}
                            alt="avatar"
                            className="d-flex align-self-center me-3"
                            height="60"
                            width="60"
                        />
                        {/* <span className={`badge ${user.status === 'ONLINE' ? 'bg-success' : 'bg-secondary'} badge-dot`}></span> */}
                    </div>
                    <div className="pt-1">
                        <p className="fw-bold mb-0 text-decoration-none">{data.roomName}</p>
                        {/* <p>Inbox</p> */}
                    </div>
                </div>
                {/* <div className="pt-1">
                    <p className="small text-muted mb-1">Just now</p>
                    <span className="badge bg-danger rounded-pill float-end">3</span>
                </div> */}
            </div>
        </>
        
    )
}
export default ChatRoom