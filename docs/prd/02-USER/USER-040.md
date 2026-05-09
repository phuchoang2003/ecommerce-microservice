# USER-040: Admin List Users

## User Story
> As an admin, I can list and search users so I can manage user accounts on the platform.

## API

### GET /api/v1/admin/users

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 1 | Page number (1-indexed) |
| size | int | 20 | Page size (max 100) |
| role | string | null | Filter by role (BUYER, SELLER) |
| status | string | null | Filter by status |
| search | string | null | Search by email or phone |
| sortBy | string | createdAt | Sort field (createdAt, email) |
| sortDir | string | desc | Sort direction (asc, desc) |

**Response (200):**
```json
{
  "users": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "user1@example.com",
      "phone": "0901234567",
      "role": "BUYER",
      "status": "ACTIVE",
      "avatarUrl": "https://cdn.example.com/avatars/user1.jpg",
      "createdAt": "2024-01-10T08:00:00Z",
      "lastLoginAt": "2024-01-20T15:30:00Z"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "email": "user2@example.com",
      "phone": "0909876543",
      "role": "SELLER",
      "status": "ACTIVE",
      "avatarUrl": null,
      "createdAt": "2024-01-12T10:00:00Z",
      "lastLoginAt": "2024-01-19T12:00:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8
  }
}
```

**Authorization:**
- Requires ADMIN role

**Side Effects:**
- None

---

## Acceptance Criteria

- [ ] Returns paginated list of users
- [ ] Can filter by role (BUYER, SELLER)
- [ ] Can filter by status
- [ ] Can search by email or phone
- [ ] Can sort by createdAt or email
- [ ] Default sort is createdAt descending
- [ ] Excludes DELETED users from results
- [ ] 401 returned if not authenticated
- [ ] 403 returned if not ADMIN role
