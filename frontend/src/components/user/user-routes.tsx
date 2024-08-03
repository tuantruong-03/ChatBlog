import { Route, Routes } from "react-router-dom"
import UserLayout from "./user-layout"
import UserHomepage from "./user-homepage"
import UserProfile from "./user-profile/user-profile"
import UserMessage from "./user-message/user-message"
import { SERVER_BASE_URL } from "../../constants/backend-server"

import SockJS from 'sockjs-client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { useAuth } from "../../hooks/auth-provider"
import { useEffect, useState } from "react"
import useApi from "../../hooks/api"

const UserRoutes = () => {
    const auth = useAuth();
    const api = useApi();


    
   
    useEffect(() => {
        const sockJsClient = new SockJS(`${SERVER_BASE_URL}/api/v1/ws`);
        const stompClient = Stomp.over(sockJsClient);
        console.log(auth.user)
        stompClient.connect(
            {
                'Authorization': `Bearer ${auth.accessToken}` // Add the JWT token here
            },
            (frame: any) => {
                // Connection success callback
                console.log('Connected: ' + frame);
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
    }, [auth.user])

    return (
        <Routes>
            <Route path="/*" element={<UserLayout />}>
                <Route index element={<UserHomepage />} />
                <Route path="profile" element={<UserProfile />} />
                <Route path="message" element={<UserMessage />} />
            </Route>

        </Routes>
    )
}

export default UserRoutes