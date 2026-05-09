# USER-030: List Addresses

## User Story
> As a logged-in user, I can view my saved addresses so I can manage my delivery locations.

## API

### GET /api/v1/users/me/addresses

**Response (200):**
```json
{
  "addresses": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "province": "Ho Chi Minh City",
      "district": "District 1",
      "ward": "Ben Nghe Ward",
      "street": "123 Nguyen Hue Street",
      "isDefault": true,
      "createdAt": "2024-01-15T10:30:00Z"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "province": "Ha Noi",
      "district": "Ba Dinh District",
      "ward": "Ngoc Khanh Ward",
      "street": "456 Hoang Quoc Viet Street",
      "isDefault": false,
      "createdAt": "2024-01-20T14:45:00Z"
    }
  ]
}
```

**Side Effects:**
- None

---

## Acceptance Criteria

- [ ] Returns all addresses for authenticated user
- [ ] Each address includes isDefault flag
- [ ] Addresses sorted by isDefault (default first), then createdAt descending
- [ ] Empty array returned if no addresses exist
- [ ] 401 returned if not authenticated
