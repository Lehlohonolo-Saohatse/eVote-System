import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8081/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// Add request interceptor to include auth token
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Authentication related API calls
export const auth = {
    login: async (email, password) => {
        const response = await api.post('/auth/login', { email, password });
        if (response.data.token) {
            localStorage.setItem('authToken', response.data.token);
            localStorage.setItem('userRole', response.data.role);
        }
        return response.data;
    },
    register: async (fullName, email, password) => {
        const response = await api.post('/auth/register', { fullName, email, password });
        return response.data;
    },
    logout: () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userRole');
    },
    isAdmin: () => {
        return localStorage.getItem('userRole') === 'ROLE_ADMIN';
    }
};

// ... rest of the file remains the same
// Candidate related API calls
export const candidates = {
    getAll: async () => {
        const response = await api.get('/candidates');
        return response.data;
    },
    
    getById: async (id) => {
        const response = await api.get(`/candidates/${id}`);
        return response.data;
    },
    
    add: async (candidate) => {
        const response = await api.post('/candidates', candidate);
        return response.data;
    },
    
    update: async (id, candidate) => {
        const response = await api.put(`/candidates/${id}`, candidate);
        return response.data;
    },
    
    delete: async (id) => {
        await api.delete(`/candidates/${id}`);
    }
};