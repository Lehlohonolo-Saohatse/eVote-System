<script>
    import { auth } from '$lib/api';
    import { goto } from '$app/navigation';

    let fullName = '';
    let email = '';
    let password = '';
    let confirmPassword = '';
    let error = '';

    async function handleRegister() {
        if (password !== confirmPassword) {
            error = 'Passwords do not match';
            return;
        }
        try {
            const response = await auth.register(fullName, email, password);
            await goto('/login');
        } catch (err) {
            error = 'Registration failed. Please try again.';
        }
    }
</script>

<div class="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-lg">
    <h1 class="text-2xl font-bold mb-6 text-center">Register</h1>
    
    {#if error}
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">{error}</div>
    {/if}

    <form on:submit|preventDefault={handleRegister} class="space-y-4">
        <div>
            <label for="fullName" class="block text-gray-700">Full Name</label>
            <input
                type="text"
                id="fullName"
                bind:value={fullName}
                required
                class="w-full px-3 py-2 border rounded-lg"
            />
        </div>
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
        <div>
            <label for="confirmPassword" class="block text-gray-700">Confirm Password</label>
            <input
                type="password"
                id="confirmPassword"
                bind:value={confirmPassword}
                required
                class="w-full px-3 py-2 border rounded-lg"
            />
        </div>
        <button type="submit" class="w-full bg-green-500 text-white py-2 rounded-lg">Register</button>
    </form>
</div>
