<script>
    import { onMount } from 'svelte';
    import { candidates } from '$lib/api';

    let candidatesList = [];
    let error = '';
    let isAdmin = false; // Simulate admin check

    let newCandidate = {
        name: '',
        description: ''
    };

    async function loadCandidates() {
        try {
            candidatesList = await candidates.getAll();
        } catch (err) {
            error = 'Failed to load candidates';
        }
    }

    async function handleAddCandidate() {
        try {
            await candidates.add(newCandidate);
            newCandidate.name = '';
            newCandidate.description = '';
            await loadCandidates();
        } catch (err) {
            error = 'Failed to add candidate';
        }
    }

    async function handleDeleteCandidate(id) {
        try {
            await candidates.delete(id);
            await loadCandidates();
        } catch (err) {
            error = 'Failed to delete candidate';
        }
    }

    onMount(loadCandidates);
</script>

<div class="container mx-auto mt-10">
    <h1 class="text-2xl font-bold mb-6 text-center">Candidate Dashboard</h1>

    {#if error}
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">{error}</div>
    {/if}

    <div class="space-y-4">
        {#if isAdmin}
            <!-- Candidate Addition Form (only for Admins) -->
            <div class="bg-white p-4 rounded-lg shadow-lg">
                <h2 class="text-xl font-semibold mb-4">Add New Candidate</h2>
                <form on:submit|preventDefault={handleAddCandidate} class="space-y-4">
                    <div>
                        <label for="name" class="block text-gray-700">Candidate Name</label>
                        <input
                            type="text"
                            id="name"
                            bind:value={newCandidate.name}
                            required
                            class="w-full px-3 py-2 border rounded-lg"
                        />
                    </div>
                    <div>
                        <label for="description" class="block text-gray-700">Description</label>
                        <input
                            type="text"
                            id="description"
                            bind:value={newCandidate.description}
                            required
                            class="w-full px-3 py-2 border rounded-lg"
                        />
                    </div>
                    <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded-lg">Add Candidate</button>
                </form>
            </div>
        {/if}

        <!-- Candidate List -->
        <div>
            <h2 class="text-xl font-semibold mb-4">Candidate List</h2>
            <ul class="space-y-4">
                {#each candidatesList as candidate}
                    <li class="bg-gray-100 p-4 rounded-lg flex justify-between items-center">
                        <div>
                            <h3 class="text-lg font-bold">{candidate.name}</h3>
                            <p>{candidate.description}</p>
                        </div>
                        {#if isAdmin}
                            <button on:click={() => handleDeleteCandidate(candidate.id)} class="bg-red-500 text-white px-4 py-2 rounded-lg">Delete</button>
                        {/if}
                    </li>
                {/each}
            </ul>
        </div>
    </div>
</div>
