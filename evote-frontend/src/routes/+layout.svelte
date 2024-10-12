<script>
    import '../app.css';

    let isLoggedIn = false;
    let isAdmin = false;

    // Check if user is logged in and is an admin
    if (typeof window !== 'undefined') {
        isLoggedIn = !!localStorage.getItem('authToken');
        isAdmin = localStorage.getItem('userRole') === 'ROLE_ADMIN';
    }

    function handleLogout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userRole');
        window.location.href = '/login';
    }
</script>

<nav class="bg-gray-800 text-white p-4">
    <div class="container mx-auto flex justify-between items-center">
        <a href="/" class="text-xl font-bold">eVote</a>
        <div class="space-x-4">
            {#if isLoggedIn}
                <a href="/dashboard">Dashboard</a>
                {#if isAdmin}
                    <a href="/admin/dashboard">Admin Dashboard</a>
                {/if}
                <button on:click={handleLogout} class="text-white">Logout</button>
            {:else}
                <a href="/login">Login</a>
                <a href="/register">Register</a>
            {/if}
        </div>
    </div>
</nav>

<main class="container mx-auto p-4">
    <slot />
</main>