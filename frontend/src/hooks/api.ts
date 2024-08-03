import axios from 'axios';
import { useAuth } from './auth-provider';
import { SERVER_BASE_URL } from '../constants/backend-server';
import Cookies from 'js-cookie';

const useApi = () => {
  const auth = useAuth();
  
  const api = axios.create({
    baseURL: SERVER_BASE_URL,
    withCredentials: true
  });

  api.interceptors.request.use(
    config => {
      if (auth.accessToken) {
        config.headers['Authorization'] = `Bearer ${auth.accessToken}`;
      }
      return config;
    },
    error => {
      return Promise.reject(error);
    }
  );

  api.interceptors.response.use(
    response => response,
    async error => {
      const originalRequest = error.config;
      if (error.response.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;
        try {
          const response = await axios.post(`${SERVER_BASE_URL}/api/v1/auth/refresh-token`, { token: auth.refreshToken }, { withCredentials: true });
          if (response.status === 200) {
            const newAccessToken = response.data.data.newAccessToken;
            Cookies.set('accessToken', newAccessToken, { path: '/' });
            auth.setAuthState((prevState : any)=> ({
              ...prevState,
              accessToken: newAccessToken
            }));
            axios.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
            return api(originalRequest);
          }
        } catch (refreshError) {
          console.error('Failed to refresh access token:', refreshError);
          auth.logout();
          window.location.href = '/login';
        }
      }
      return Promise.reject(error);
    }
  );

  return api;
};

export default useApi;
