import React, { useState } from 'react';
import '../../App.css';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { userLoginValidation } from '../../utils/vadliation-schema';
import { useAuth } from '../../hooks/auth-provider';
import { GoogleLogin, useGoogleLogin } from '@react-oauth/google';
import axios from 'axios';
import Cookies from 'js-cookie';
import {  SERVER_BASE_URL } from '../../constants/backend-server';
import { useNavigate } from 'react-router-dom';
import FacebookLogin from '@greatsumini/react-facebook-login';
import { IResolveParams, LoginSocialFacebook, LoginSocialGoogle } from 'reactjs-social-login';
const facebookClientId = process.env.REACT_APP_FACEBOOK_CLIENT_ID || '';
const googleClientId = process.env.REACT_APP_GOOGLE_CLIENT_ID ||''


const Login: React.FC = () => {
    const navigate = useNavigate();

    const [showPassword, setShowPassword] = useState(false);
    const [message, setMessage] = useState<any | null>(null);


    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const auth = useAuth();

    const initialValues = {
        username: '',
        password: ''
    };



    const localLogin = async (values: any) => {
        try {
            const msg = await auth.login(values);
            setMessage(msg);
        } catch (err) {
            throw err;
        }
    };

    const googleLogin = useGoogleLogin({
        onSuccess: async (response) => {
            console.log(response)
            const { access_token } = response;
            console.log(access_token)

            try {
                const response = await axios.post(`${SERVER_BASE_URL}/api/v1/auth/google-login`, { token: access_token })
                if (response.status == 200) {
                    console.log(response)
                    const accessToken = response.data.data.accessToken;
                    const refreshToken = response.data.data.refreshToken;
                    Cookies.set('accessToken', accessToken, { path: '/' });
                    Cookies.set('refreshToken', refreshToken, { path: '/' });


                    auth.setAuthState((prevState: any) => ({
                        ...prevState,
                        isAuthenticated: true,
                        accessToken,
                        refreshToken,
                    }));
                    navigate("/");
                }

            } catch (error) {
                console.error('Error fetching user info:', error);
            }
        },


    })
    const facebookLogin = async (response: any) => {
        console.log("response ", response)
        if (response.accessToken) {
            try {
                const res = await axios.post(`${SERVER_BASE_URL}/api/v1/auth/facebook-login`, { token: response.accessToken });
                if (res.status === 200) {
                    const accessToken = res.data.data.accessToken;
                    const refreshToken = res.data.data.refreshToken;
                    Cookies.set('accessToken', accessToken, { path: '/' });
                    Cookies.set('refreshToken', refreshToken, { path: '/' });

                    auth.setAuthState((prevState: any) => ({
                        ...prevState,
                        isAuthenticated: true,
                        accessToken,
                        refreshToken,
                    }));
                    navigate("/");
                }
            } catch (error) {
                console.error('Error logging in with Facebook:', error);
            }
        }
    }

    return (
        <section className="container forms">
            <div className="form signup">
                <div className="form-content">
                    <header>Register</header>
                    <Formik
                        initialValues={initialValues}
                        validationSchema={userLoginValidation}
                        onSubmit={localLogin}
                    >
                        {({ isSubmitting, setFieldValue }) => (
                            <Form className="custom-form">
                                {message && <div className="alert alert-danger">{message}</div>}
                                <div className="field input-field">
                                    <Field type="text" name="username" placeholder="Username" className="input" />
                                    <ErrorMessage name="username" component="div" className="error" />
                                </div>
                                <div className="field input-field">
                                    <Field type="password" name="password" placeholder="Password" className="password" />
                                    <ErrorMessage name="password" component="div" className="error" />
                                </div>
                                <div className="form-link">
                                    <a href="#" className="forgot-pass">Forgot password?</a>
                                </div>
                                <div className="field button-field text-center">
                                    <button type="submit" disabled={isSubmitting} className={`btn ${isSubmitting ? 'disabled' : ''}`}>
                                        {isSubmitting ? 'Signing...' : 'Login'}
                                    </button>
                                </div>

                            </Form>
                        )}
                    </Formik>

                    <div className="form-link">
                        <span>Don't have an account? <a href="/register" className="link signup-link">Register</a></span>
                    </div>
                </div>
                <div className="line"></div>

                <div className="media-options">
                    <FacebookLogin
                        appId={facebookClientId}
                        scope='email,public_profile'
                        onSuccess={facebookLogin}
                        fields='email'
                        onFail={(error) => console.error('Facebook login error:', error)}
                        render={({ onClick }) => (
                            <a onClick={onClick} style={{ cursor: 'pointer' }} className="field facebook">
                                <img src="https://www.logo.wine/a/logo/Facebook/Facebook-f_Logo-White-Dark-Background-Logo.wine.svg" alt="Facebook Icon" className="facebook-icon" />
                                <span>Login with Facebook</span>
                            </a>
                        )}
                    />
                </div>
                <div className="media-options">
                    <a onClick={() => googleLogin()} style={{ cursor: 'pointer' }} className="field google">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/1024px-Google_%22G%22_logo.svg.png" alt="Google Icon" className="google-img" />
                        <span>Login with Google</span>
                    </a>
                </div>

            </div>
        </section>
    );
}

export default Login;
