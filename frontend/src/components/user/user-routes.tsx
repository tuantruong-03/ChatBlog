import { Route, Routes } from "react-router-dom"
import UserLayout from "./user-layout"
import UserHomepage from "./user-homepage"
import UserProfile from "./user-profile/user-profile"

import UserChatLayout from "./user-chat/layout"
import { ChatProvider } from "../../hooks/chat-provider"
import TestContext from "./test-context"

const UserRoutes = () => {


    return (
        <ChatProvider>
            <Routes>
                <Route path="/*" element={<UserLayout />}>
                    <Route index element={<UserHomepage />} />
                    <Route path="profile" element={<UserProfile />} />
                    <Route path="message" element={<UserChatLayout />} />
                    <Route path="test-context" element={<TestContext/>}/>
                </Route>

            </Routes>
        </ChatProvider>
    )
}

export default UserRoutes