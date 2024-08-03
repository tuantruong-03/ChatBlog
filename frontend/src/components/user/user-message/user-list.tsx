
interface UserBoxProps {
    user: any;
}
const UserList = (props: UserBoxProps) => {
    const {user } = props;
    return (
        <li key={user.userId} className="p-2 border-bottom" style={{cursor: 'pointer'}}>
            <div className="d-flex justify-content-between">
                <div className="d-flex flex-row">
                    <div>
                        <img
                            src={user.profilePicture}
                            alt="avatar"
                            className="d-flex align-self-center me-3"
                            width="60"
                        />
                        <span className={`badge ${user.status === 'ONLINE' ? 'bg-success' : 'bg-secondary'} badge-dot`}></span>
                    </div>
                    <div className="pt-1">
                        <p className="fw-bold mb-0 text-decoration-none">{user.firstName + user.lastName}</p>
                        {/* <p>Inbox</p> */}
                    </div>
                </div>
                {/* <div className="pt-1">
                    <p className="small text-muted mb-1">Just now</p>
                    <span className="badge bg-danger rounded-pill float-end">3</span>
                </div> */}
            </div>
        </li>
    )
}
export default UserList