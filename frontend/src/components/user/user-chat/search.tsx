import { ListGroup } from "react-bootstrap";
import { SERVER_BASE_URL } from "../../../constants/backend-server";
import useApi from "../../../hooks/api";

interface UserSearchProps {
    user: any;
}
const UserSearch = (props: UserSearchProps) => {
    const {user } = props;


    return (
            <div className="d-flex justify-content-between">
                <div className="d-flex flex-row">
                    <div>
                        <img
                            src={user.profilePicture}
                            alt="avatar"
                            className="d-flex align-self-center me-3"
                            width="60"
                            height="60"
                        />
                        <span className={`badge ${user.status === 'ONLINE' ? 'bg-success' : 'bg-secondary'} badge-dot`}></span>
                    </div>
                    <div className="pt-1">
                        <p className="fw-bold mb-0 text-decoration-none">{user?.firstName + user?.lastName}</p>
                        {/* <p>Inbox</p> */}
                    </div>
                </div>
                {/* <div className="pt-1">
                    <p className="small text-muted mb-1">Just now</p>
                    <span className="badge bg-danger rounded-pill float-end">3</span>
                </div> */}
            </div>
    )
}
export default UserSearch