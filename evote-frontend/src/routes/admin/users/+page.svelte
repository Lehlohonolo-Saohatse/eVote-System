
// src/routes/admin/users/+page.svelte
<script>
    import { onMount } from 'svelte';
    import { Alert, AlertDescription } from '$lib/components/ui/alert';
    import { Button } from '$lib/components/ui/button';
    import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '$lib/components/ui/table';

    let users = [];
    let error = '';

    async function fetchUsers() {
        try {
            const response = await fetch('http://localhost:8081/api/admin/users', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            });
            if (!response.ok) throw new Error('Failed to fetch users');
            users = await response.json();
        } catch (err) {
            error = err.message;
        }
    }

    async function handleDeleteUser(id) {
        if (!confirm('Are you sure you want to delete this user?')) return;
        
        try {
            const response = await fetch(`http://localhost:8081/api/admin/users/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            });
            if (!response.ok) throw new Error('Failed to delete user');
            await fetchUsers();
        } catch (err) {
            error = err.message;
        }
    }

    async function handleUpdateRole(id, newRole) {
        try {
            const response = await fetch(`http://localhost:8081/api/admin/users/${id}/role`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newRole)
            });
            if (!response.ok) throw new Error('Failed to update user role');
            await fetchUsers();
        } catch (err) {
            error = err.message;
        }
    }

    onMount(fetchUsers);
</script>

<div class="space-y-6">
    <h1 class="text-2xl font-bold">User Management</h1>

    {#if error}
        <Alert variant="destructive">
            <AlertDescription>{error}</AlertDescription>
        </Alert>
    {/if}

    <Table>
        <TableHeader>
            <TableRow>
                <TableHead>Name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Role</TableHead>
                <TableHead>Actions</TableHead>
            </TableRow>
        </TableHeader>
        <TableBody>
            {#each users as user}
                <TableRow>
                    <TableCell>{user.fullName}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>
                        <select 
                            value={user.role}
                            on:change={(e) => handleUpdateRole(user.id, e.target.value)}
                            class="border rounded px-2 py-1"
                        >
                            <option value="ROLE_USER">User</option>
                            <option value="ROLE_ADMIN">Admin</option>
                        </select>
                    </TableCell>
                    <TableCell>
                        <Button 
                            variant="destructive" 
                            size="sm"
                            on:click={() => handleDeleteUser(user.id)}
                        >
                            Delete
                        </Button>
                    </TableCell>
                </TableRow>
            {/each}
        </TableBody>
    </Table>
</div>