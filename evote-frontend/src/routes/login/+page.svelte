<script>
    import { auth } from '$lib/api';
    import { goto } from '$app/navigation';

    let email = '';
    let password = '';
    let error = '';

    async function handleLogin() {
        try {
            error = '';
            const response = await auth.login(email, password);
            
            if (response.token) {
                localStorage.setItem('authToken', response.token);
                localStorage.setItem('user', JSON.stringify(response.user));
                await goto('/dashboard');
            }
        } catch (err) {
            error = 'Invalid email or password';
        }
    }
</script>

<div class="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-lg">
    <h1 class="text-2xl font-bold mb-6 text-center">Login</h1>
    
    {#if error}
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">{error}</div>
    {/if}

    <form on:submit|preventDefault={handleLogin} class="space-y-4">
        <div>
            <label for="email" class="block text-gray-700">Email</label>
            <input
                type="email"
                id="email"
                bind:value={email}
                required
                class="w-full px-3 py-2 border rounded-lg"
            />
        </div>
        
        <div>
            <label for="password" class="block text-gray-700">Password</label>
            <input
                type="password"
                id="password"
                bind:value={password}
                required
                class="w-full px-3 py-2 border rounded-lg"
            />
        </div>
        
        <button type="submit" class="w-full bg-blue-500 text-white py-2 rounded-lg">Login</button>
    </form>
</div>
