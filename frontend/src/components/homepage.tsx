import { useEffect, useState } from "react";
import useApi from "../hooks/api";
import { SERVER_BASE_URL } from "../constants/backend-server";

const Homepage = () => {
    console.log("Asd")
    const api = useApi();
    const [username, setUsername] = useState<string>("");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await api.get(`/api/v1/users/truonganhtuan3092@gmail.com`)
                if (response.status == 200) {
                    const data = response.data.data;
                    console.log(data.username)
                    setUsername(data.username)
                }
            } catch (err) {
                console.log(err)
            }

        }
        fetchData();
    }, [])

    return (
        <>Welcome to homepage {username}</>
    )
}
export default Homepage;