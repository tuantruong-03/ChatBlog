import { useEffect, useState } from "react";
import useApi from "./api";


const useUserData = () => {
    const api = useApi();
    const [userData, setUserData ] = useState()
    const [error,setError] = useState(null)
    const [loading,setLoading] = useState(false)
    useEffect(() => {
        const fetchUserData = async () => {
            setLoading(true)
            try {
                const response = await api.get(`/api/v1/users/me`)  
                if (response.status == 200) {
                    const data = response.data.data;
                    // console.log(data)
                    setUserData(data)
        
                }
            } catch (err) {
                setError(error)
            } finally {
                setLoading(false)
            }
        }
        fetchUserData()
    },[])
    return {userData, error, loading}
}

export default useUserData
