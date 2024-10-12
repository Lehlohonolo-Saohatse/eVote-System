
<!-- src/routes/admin/dashboard/+page.svelte -->
<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { Alert, AlertDescription } from '$lib/components/ui/alert';
    import { Button } from '$lib/components/ui/button';
    import { Card, CardContent, CardHeader, CardTitle } from '$lib/components/ui/card';
    import { Input } from '$lib/components/ui/input';
    import { AlertCircle, Trash2, Plus, Image as ImageIcon } from 'lucide-svelte';

    let candidates = [];
    let error = '';
    let loading = false;
    let newCandidate = {
        name: '',
        description: '',
        image: null
    };

    // Check authentication on mount
    onMount(() => {
        const token = localStorage.getItem('authToken');
        const isAdmin = localStorage.getItem('isAdmin');
        
        if (!token || !isAdmin) {
            goto('/admin/login');
            return;
        }
        
        fetchCandidates();
    });

    async function fetchCandidates() {
        try {
            loading = true;
            const response = await fetch('http://localhost:8081/api/candidates');
            candidates = await response.json();
        } catch (err) {
            error = 'Failed to load candidates';
        } finally {
            loading = false;
        }
    }

    async function handleAddCandidate() {
        try {
            const formData = new FormData();
            formData.append('name', newCandidate.name);
            formData.append('description', newCandidate.description);
            if (newCandidate.image) {
                formData.append('image', newCandidate.image);
            }

            const response = await fetch('http://localhost:8081/api/candidates', {
                method: 'POST',
                body: formData,
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            });

            if (!response.ok) throw new Error('Failed to add candidate');

            newCandidate = { name: '', description: '', image: null };
            await fetchCandidates();
        } catch (err) {
            error = 'Failed to add candidate';
        }
    }

    async function handleDeleteCandidate(id) {
        try {
            const response = await fetch(`http://localhost:8081/api/candidates/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            });

            if (!response.ok) throw new Error('Failed to delete candidate');

            await fetchCandidates();
        } catch (err) {
            error = 'Failed to delete candidate';
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
                <AlertCircle class="h-4 w-4" />
                <AlertDescription>{error}</AlertDescription>
            </Alert>
        {/if}

        <!-- Add Candidate Form -->
        <Card class="mb-8">
            <CardHeader>
                <CardTitle>Add New Candidate</CardTitle>
            </CardHeader>
            <CardContent>
                <form on:submit|preventDefault={handleAddCandidate} class="space-y-4">
                    <div>
                        <Input
                            placeholder="Candidate Name"
                            bind:value={newCandidate.name}
                            required
                        />
                    </div>
                    <div>
                        <textarea
                            class="w-full min-h-[100px] p-3 border rounded-md"
                            placeholder="Candidate Description"
                            bind:value={newCandidate.description}
                            required
                        ></textarea>
                    </div>
                    <div class="flex items-center gap-4">
                        <input
                            type="file"
                            accept="image/*"
                            on:change={(e) => newCandidate.image = e.target.files[0]}
                            class="hidden"
                            id="image-upload"
                        />
                        <Button
                            type="button"
                            variant="outline"
                            on:click={() => document.getElementById('image-upload').click()}
                        >
                            <ImageIcon class="mr-2 h-4 w-4" />
                            Upload Image
                        </Button>
                        {#if newCandidate.image}
                            <span class="text-sm text-gray-600">
                                Selected: {newCandidate.image.name}
                            </span>
                        {/if}
                    </div>
                    <Button type="submit" class="w-full">
                        <Plus class="mr-2 h-4 w-4" />
                        Add Candidate
                    </Button>
                </form>
            </CardContent>
        </Card>

        <!-- Candidates List -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            {#each candidates as candidate (candidate.id)}
                <Card>
                    <CardHeader class="flex flex-row items-center justify-between">
                        <CardTitle>{candidate.name}</CardTitle>
                        <Button
                            variant="destructive"
                            size="icon"
                            on:click={() => handleDeleteCandidate(candidate.id)}
                        >
                            <Trash2 class="h-4 w-4" />
                        </Button>
                    </CardHeader>
                    <CardContent>
                        <div class="space-y-4">
                            {#if candidate.imagePath}
                                <img
                                    src={`http://localhost:8081/${candidate.imagePath}`}
                                    alt={candidate.name}
                                    class="w-full h-48 object-cover rounded-md"
                                />
                            {/if}
                            <p class="text-gray-600">{candidate.description}</p>
                            {#if candidate.voteCount !== undefined}
                                <p class="text-sm font-semibold">
                                    Votes: {candidate.voteCount}
                                </p>
                            {/if}
                        </div>
                    </CardContent>
                </Card>
            {/each}
        </div>
    </main>
</div>