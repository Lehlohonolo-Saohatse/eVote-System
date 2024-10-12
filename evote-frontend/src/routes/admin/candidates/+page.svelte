// src/routes/admin/+page.svelte
<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { Alert, AlertDescription } from '$lib/components/ui/alert';
    import { Button } from '$lib/components/ui/button';
    import { Card, CardContent, CardHeader, CardTitle } from '$lib/components/ui/card';
    import { Tabs, TabsList, TabsTrigger, TabsContent } from '$lib/components/ui/tabs';

    let candidates = [];
    let users = [];
    let error = '';
    let loading = false;

    // Check authentication on mount
    onMount(() => {
        const token = localStorage.getItem('authToken');
        const isAdmin = localStorage.getItem('isAdmin');
        
        if (!token || !isAdmin) {
            goto('/admin/login');
            return;
        }
        
        fetchData();
    });

    async function fetchData() {
        try {
            loading = true;
            await Promise.all([
                fetchCandidates(),
                fetchUsers()
            ]);
        } catch (err) {
            error = 'Failed to load data';
        } finally {
            loading = false;
        }
    }

    async function fetchCandidates() {
        const response = await fetch('http://localhost:8081/api/candidates', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            }
        });
        candidates = await response.json();
    }

    async function fetchUsers() {
        const response = await fetch('http://localhost:8081/api/admin/users', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            }
        });
        users = await response.json();
    }

    async function handleDeleteCandidate(id) {
        try {
            await fetch(`http://localhost:8081/api/candidates/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            });
            await fetchCandidates();
        } catch (err) {
            error = 'Failed to delete candidate';
        }
    }

    async function handleDeleteUser(id) {
        try {
            await fetch(`http://localhost:8081/api/admin/users/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            });
            await fetchUsers();
        } catch (err) {
            error = 'Failed to delete user';
        }
    }

    function handleLogout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('isAdmin');
        goto('/admin/login');
    }
</script>

<div class="min-h-screen bg-gray-50">
    <nav class="bg-white shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between h-16">
                <div class="flex items-center">
                    <h1 class="text-xl font-bold">Admin Dashboard</h1>
                </div>
                <div class="flex items-center">
                    <Button variant="outline" on:click={handleLogout}>
                        Logout
                    </Button>
                </div>
            </div>
        </div>
    </nav>

    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        {#if error}
            <Alert variant="destructive" class="mb-6">
                <AlertDescription>{error}</AlertDescription>
            </Alert>
        {/if}

        <Tabs defaultValue="candidates" class="w-full">
            <TabsList class="grid w-full grid-cols-2">
                <TabsTrigger value="candidates">Candidates</TabsTrigger>
                <TabsTrigger value="users">Users</TabsTrigger>
            </TabsList>

            <TabsContent value="candidates">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {#each candidates as candidate (candidate.id)}
                        <Card>
                            <CardHeader class="flex flex-row items-center justify-between">
                                <CardTitle>{candidate.name}</CardTitle>
                                <Button
                                    variant="destructive"
                                    on:click={() => handleDeleteCandidate(candidate.id)}
                                >
                                    Delete
                                </Button>
                            </CardHeader>
                            <CardContent>
                                <p class="text-gray-600">{candidate.description}</p>
                                <p class="text-sm mt-2">Party: {candidate.party}</p>
                                <p class="text-sm">Election: {candidate.election}</p>
                                {#if candidate.voteCount !== undefined}
                                    <p class="text-sm font-semibold mt-2">
                                        Votes: {candidate.voteCount}
                                    </p>
                                {/if}
                            </CardContent>
                        </Card>
                    {/each}
                </div>
            </TabsContent>

            <TabsContent value="users">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {#each users as user (user.id)}
                        <Card>
                            <CardHeader class="flex flex-row items-center justify-between">
                                <CardTitle>{user.fullName}</CardTitle>
                                <Button
                                    variant="destructive"
                                    on:click={() => handleDeleteUser(user.id)}
                                >
                                    Delete
                                </Button>
                            </CardHeader>
                            <CardContent>
                                <p class="text-gray-600">{user.email}</p>
                                <p class="text-sm mt-2">Role: {user.role}</p>
                            </CardContent>
                        </Card>
                    {/each}
                </div>
            </TabsContent>
        </Tabs>
    </main>
</div>