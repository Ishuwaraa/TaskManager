export interface UserLoginRequest {
    email: string, 
    password: string
}

export interface UserLoginResponse {
    accessToken: string,
    refreshToken: string,
    error: string
}

export interface UserRegisterRequest {
    name: string,
    email: string,
    password: string
}

export interface UserRegisterResponse {
    email: string,
    error: string
}