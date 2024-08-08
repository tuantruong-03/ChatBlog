import { useEffect, useState } from "react";
import { useAuth } from "../../hooks/auth-provider";
import useApi from "../../hooks/api";

const UserHomepage = () => {
    const api = useApi();
    const auth = useAuth();
    const [user, setUser] = useState<any>();
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await api.get(`/api/v1/users/me`)  
                if (response.status == 200) {
                    const user = response.data.data;
                    setUser(user);
                    // console.log(data)
   
                }
            } catch (err) {
                console.log(err)
            }

        }
        fetchData();
    }, [])


    return (
        <>
        Welcome to homepage {user?.username}
        <button onClick={() => auth.logout()}>Log out</button>
        </>

    )
}
export default UserHomepage;