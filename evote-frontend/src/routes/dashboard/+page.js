// src/routes/dashboard/+page.js
export async function load({ parent }) {
    const token = localStorage.getItem('authToken');
    if (!token) {
        throw redirect(307, '/login');
    }
    return {};
}