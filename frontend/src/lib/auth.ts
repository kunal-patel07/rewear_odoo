const API_BASE_URL = 'http://localhost:9186'

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  name: string
  email: string
  password: string
  username: string
}

export interface LoginResponse {
  timestamp: string
  data: {
    email: string
    password: string
    token: string
  }
  error: string | null
}

export interface RegisterResponse {
  timestamp: string
  data: {
    id: number
    name: string
    email: string
    password: string
    username: string
  }
  error: string | null
}

export interface ApiError {
  message: string
  status: number
}

// Token management functions
export const getToken = (): string | null => {
  if (typeof window !== 'undefined') {
    return localStorage.getItem('auth_token')
  }
  return null
}

export const setToken = (token: string): void => {
  if (typeof window !== 'undefined') {
    localStorage.setItem('auth_token', token)
  }
}

export const removeToken = (): void => {
  if (typeof window !== 'undefined') {
    localStorage.removeItem('auth_token')
  }
}

export const isTokenExpired = (token: string): boolean => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    const currentTime = Date.now() / 1000
    return payload.exp < currentTime
  } catch (error) {
    return true // If we can't decode, consider it expired
  }
}

export const isAuthenticated = (): boolean => {
  const token = getToken()
  if (!token) return false
  
  // Check if token is expired
  if (isTokenExpired(token)) {
    removeToken() // Clean up expired token
    return false
  }
  
  return true
}

export const getUserInfoFromToken = (): {
  email?: string
  name?: string
  username?: string
  sub?: string
  role?: string
} | null => {
  const token = getToken()
  if (!token || isTokenExpired(token)) {
    return null
  }
  
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return {
      email: payload.email,
      name: payload.name,
      username: payload.username,
      sub: payload.sub,
      role: payload.role,
    }
  } catch (error) {
    console.error('Error decoding token:', error)
    return null
  }
}

// Enhanced logout function that doesn't force redirect
export const logout = (redirectToLogin = false): void => {
  removeToken()
  if (redirectToLogin && typeof window !== 'undefined') {
    window.location.href = '/login'
  }
}

// API functions
export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
  const response = await fetch(`${API_BASE_URL}/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(credentials),
  })

  if (!response.ok) {
    throw new ApiError({
      message: `Login failed: ${response.statusText}`,
      status: response.status,
    })
  }

  const data = await response.json()
  
  if (data.error) {
    throw new ApiError({
      message: data.error,
      status: response.status,
    })
  }

  // Store token if login successful
  if (data.data?.token) {
    setToken(data.data.token)
  }

  return data
}

export const register = async (userData: RegisterRequest): Promise<RegisterResponse> => {
  const response = await fetch(`${API_BASE_URL}/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  })

  if (!response.ok) {
    throw new ApiError({
      message: `Registration failed: ${response.statusText}`,
      status: response.status,
    })
  }

  const data = await response.json()
  
  if (data.error) {
    throw new ApiError({
      message: data.error,
      status: response.status,
    })
  }

  return data
}

// Get authorization headers for authenticated requests
export const getAuthHeaders = (): Record<string, string> => {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
} 