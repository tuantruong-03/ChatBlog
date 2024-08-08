import { createContext, useState, useEffect, ReactNode, useContext, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import axios from 'axios';
import { SERVER_BASE_URL } from '../constants/backend-server';
import { CompatClient, Stomp } from '@stomp/stompjs';

// Helper function to get tokens
const getAccessToken = () => Cookies.get('accessToken') || null;
const getRefreshToken = () => Cookies.get('refreshToken') || null;

// Define AuthState interface
interface AuthState {
    isAuthenticated: boolean;
    accessToken: string | null;
    refreshToken: string | null;
    login: (input: object) => Promise<any>;
    logout: () => Promise<void>;
    setAuthState: React.Dispatch<React.SetStateAction<AuthState>>;
    sockJsClient: any;
    stompClient: CompatClient | null;
    user: any;
}

// Initial state
const authStateInit: AuthState = {
    isAuthenticated: !!getAccessToken(),
    accessToken: getAccessToken(),
    refreshToken: getRefreshToken(),
    login: async (input: object) => Promise.resolve(),
    logout: async () => Promise.resolve(),
    setAuthState: () => { },
    sockJsClient: null,
    stompClient: null,
    user: null,
};

const AuthContext = createContext<AuthState>(authStateInit);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
    const [authState, setAuthState] = useState<AuthState>(authStateInit);
    const navigate = useNavigate();

    // Define login function
    const login = async (input: object): Promise<any> => {
        try {
            const response = await axios.post(`${SERVER_BASE_URL}/api/v1/auth/login`, input, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 200) {
                const { accessToken, refreshToken, roles } = response.data.data;
                Cookies.set('accessToken', accessToken, { path: '/', secure: true });
                Cookies.set('refreshToken', refreshToken, { path: '/', secure: true });
                
                // Initialize SockJS and Stomp client

                setAuthState((prev: any) => ({
                    ...prev,
                    isAuthenticated: true,
                    accessToken,
                    refreshToken,
                    login,
                    logout,
                    setAuthState,
                }));
                navigate('/');
            } else {
                return response.data.message;
            }
        } catch (err: any) {
            console.error('Login Error: ', err);
            return err.response?.data?.message || 'An error occurred';
        }
    };

    // Define logout function
    const logout = async () => {
        try {
            const { refreshToken } = authState;
            console.log(refreshToken)
            const response = await axios.delete(`${SERVER_BASE_URL}/api/v1/auth/logout`, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: { refreshToken },
            });

            if (response.status === 204) {
                Cookies.remove('accessToken');
                Cookies.remove('refreshToken');
                authState.stompClient?.disconnect();
                
                setAuthState({
                    isAuthenticated: false,
                    accessToken: null,
                    refreshToken: null,
                    sockJsClient: null,
                    stompClient: null,
                    user: null,
                    login,
                    logout,
                    setAuthState,
                });
                navigate('/login');
            }
        } catch (err: any) {
            console.error('Logout Error: ', err);
            return err.response?.data?.message || 'An error occurred';
        }
    };

    const authContextValue = useMemo(() => ({
        ...authState,
        setAuthState,
        login,
        logout
    }), [authState, setAuthState]);

    return (
        <AuthContext.Provider value={authContextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};

export default AuthProvider;
