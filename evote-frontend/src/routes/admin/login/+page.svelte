<!-- src/routes/admin/login/+page.svelte -->
<script>
    import { goto } from '$app/navigation';
    import { Alert, AlertDescription } from '$lib/components/ui/alert';
    import { Button } from '$lib/components/ui/button';
    import { Input } from '$lib/components/ui/input';
    import { Label } from '$lib/components/ui/label';
    import { AlertCircle } from 'lucide-svelte';

    let email = '';
    let password = '';
    let error = '';
    let loading = false;

    async function handleLogin() {
        loading = true;
        error = '';
        
        try {
            const response = await fetch('http://localhost:8081/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (!response.ok) throw new Error(data.message || 'Login failed');

            // Store the token and user info
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('isAdmin', 'true'); // You should get this from the backend
            
            // Redirect to admin dashboard
            goto('/admin/dashboard');
        } catch (err) {
            error = err.message || 'Login failed';
        } finally {
            loading = false;
        }
    }
</script>

<div class="flex min-h-screen items-center justify-center bg-gray-50">
    <div class="w-full max-w-md space-y-8 p-8">
        <div class="text-center">
            <h2 class="text-3xl font-bold tracking-tight">Admin Login</h2>
            <p class="mt-2 text-sm text-gray-600">Enter your credentials to access the admin panel</p>
        </div>

        {#if error}
            <Alert variant="destructive">
                <AlertCircle class="h-4 w-4" />
                <AlertDescription>{error}</AlertDescription>
            </Alert>
        {/if}

        <form on:submit|preventDefault={handleLogin} class="mt-8 space-y-6">
            <div class="space-y-4">
                <div>
                    <Label for="email">Email</Label>
                    <Input
                        id="email"
                        type="email"
                        bind:value={email}
                        required
                        placeholder="admin@example.com"
                    />
                </div>

                <div>
                    <Label for="password">Password</Label>
                    <Input
                        id="password"
                        type="password"
                        bind:value={password}
                        required
                        placeholder="••••••••"
                    />
                </div>
            </div>

            <Button
                type="submit"
                class="w-full"
                disabled={loading}
            >
                {loading ? 'Signing in...' : 'Sign in'}
            </Button>
        </form>
    </div>
</div>