// src/routes/admin/+layout.svelte
<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';

    onMount(() => {
        const token = localStorage.getItem('authToken');
        const userRole = localStorage.getItem('userRole');
        
        if (!token || userRole !== 'ROLE_ADMIN') {
            goto('/login');
        }
    });
</script>

<div class="min-h-screen bg-gray-100">
    <nav class="bg-white shadow-sm">
        <div class="max-w-7xl mx-auto px-4">
            <div class="flex justify-between h-16">
                <div class="flex">
                    <a href="/admin/dashboard" class="px-3 py-2 rounded-md text-sm font-medium text-gray-900">
                        Dashboard
                    </a>
                    <a href="/admin/users" class="px-3 py-2 rounded-md text-sm font-medium text-gray-900">
                        Users
                    </a>
                    <a href="/admin/candidates" class="px-3 py-2 rounded-md text-sm font-medium text-gray-900">
                        Candidates
                    </a>
                </div>
                <button 
                    on:click={() => {
                        localStorage.removeItem('authToken');
                        localStorage.removeItem('userRole');
                        goto('/login');
                    }}
                    class="px-3 py-2 rounded-md text-sm font-medium text-red-600"
                >
                    Logout
                </button>
            </div>
        </div>
    </nav>
    
    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <slot />
    </main>
</div>
