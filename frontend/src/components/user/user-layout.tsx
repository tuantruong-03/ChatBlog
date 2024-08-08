import { Outlet } from "react-router-dom";
import Header from "../header";
import Footer from "../footer";
import { useAuth } from "../../hooks/auth-provider";
import useApi from "../../hooks/api";
import { SERVER_BASE_URL } from "../../constants/backend-server"

import SockJS from 'sockjs-client';
import {  Stomp } from '@stomp/stompjs';
import { useEffect  } from "react"
const UserLayout = () => {
    const  auth = useAuth();
    const api = useApi();
    useEffect(() => {
        let userId: string = "";
        const sockJsClient = new SockJS(`${SERVER_BASE_URL}/api/v1/ws`);
        const stompClient = Stomp.over(sockJsClient);
        const fetchData = async () => {
            try {
                const response = await api.get(`/api/v1/users/me`)  
                if (response.status == 200) {
                    const data = response.data.data;
                    // console.log(data)
                    userId = data.userId;
                    auth.setAuthState((prevState) => ({
                        ...prevState,
                        user: data
                    }));
                }
            } catch (err) {
                console.log(err)
            }

        }
        fetchData();
        if (auth.stompClient) {
            console.log("STOMP Client already initialized");
            return;
        }
        // console.log(auth.user)
        stompClient.connect(
            {
                'Authorization': `Bearer ${auth.accessToken}` // Add the JWT token here
            },
            (frame: any) => {
                // Connection success callback
                console.log('cConnected: ' + frame);
                auth.setAuthState((prevState) => ({
                    ...prevState,
                    sockJsClient,
                    stompClient,
                    isAuthenticated: true,
                }));
            },
            (error: any) => {
                // Connection error callback
                console.error('Error: ' + error);
            }
        );
        
    }, [])

    return (
        <>
            <Header />
            <main id='main' className="container py-5 flex-grow-1 ">
                <Outlet /> {/* This is where nested routes will render */}
            </main>
            <Footer />
        </>



    );
}
export default UserLayout;