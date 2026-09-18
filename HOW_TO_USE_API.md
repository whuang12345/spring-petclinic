# How to Use Spring PetClinic REST API - Complete Guide

## Table of Contents
1. [Getting Started](#getting-started)
2. [Using cURL](#using-curl)
3. [Using Postman](#using-postman)
4. [Complete Workflow Examples](#complete-workflow-examples)
5. [Error Handling](#error-handling)
6. [Tips and Tricks](#tips-and-tricks)

---

## Getting Started

### Step 1: Start the Application

```bash
# Navigate to project directory
cd C:\Eclipse\workspace\spring-petclinic

# Using Maven
mvn spring-boot:run

# OR using Gradle
gradle bootRun
```

The application starts at: `http://localhost:8080`
API Base URL: `http://localhost:8080/api`

### Step 2: Verify API is Running

Open your browser and go to:
```
http://localhost:8080/api/owners
```

You should see a JSON response with owners (if any exist in the database).

---

## Using cURL

### 1. LIST ALL OWNERS

```bash
curl http://localhost:8080/api/owners
```

**Example Response:**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Smith",
      "address": "123 Main Street",
      "city": "New York",
      "telephone": "5551234567",
      "pets": []
    }
  ],
  "pageable": { ... },
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 10
}
```

---

### 2. CREATE A NEW OWNER

```bash
curl
```

**Response (201 Created):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Smith",
  "address": "123 Main Street",
  "city": "New York",
  "telephone": "5551234567",
  "pets": []
}
```

**Note:** Save the `id` (1) for future requests.

---

### 3. GET A SPECIFIC OWNER

```bash
curl http://localhost:8080/api/owners/1
```

---

### 4. UPDATE AN OWNER

```bash
curl -X PUT http://localhost:8080/api/owners/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "address": "456 Oak Avenue",
    "city": "Boston",
    "telephone": "6171234567"
  }'
```

---

### 5. SEARCH OWNERS BY LAST NAME

```bash
curl "http://localhost:8080/api/owners/search?lastName=Smith"
```

---

### 6. GET ALL PET TYPES

```bash
curl http://localhost:8080/api/pet-types
```

**Example Response:**
```json
[
  {
    "id": 1,
    "name": "Cat"
  },
  {
    "id": 2,
    "name": "Dog"
  },
  {
    "id": 3,
    "name": "Hamster"
  }
]
```

---

### 7. CREATE A PET FOR AN OWNER

First, get a pet type ID from the response above (e.g., 1 for Cat).

```bash
curl -X POST http://localhost:8080/api/owners/1/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fluffy",
    "birthDate": "2020-05-15",
    "type": {
      "id": 1,
      "name": "Cat"
    }
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Fluffy",
  "birthDate": "2020-05-15",
  "type": {
    "id": 1,
    "name": "Cat"
  },
  "visits": []
}
```

**Note:** Save the pet ID (1) for creating visits.

---

### 8. GET ALL PETS FOR AN OWNER

```bash
curl http://localhost:8080/api/owners/1/pets
```

---

### 9. CREATE A VISIT FOR A PET

```bash
curl -X POST http://localhost:8080/api/owners/1/pets/1/visits \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-02-15",
    "description": "Annual checkup and vaccinations"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "date": "2025-02-15",
  "description": "Annual checkup and vaccinations"
}
```

---

### 10. GET ALL VISITS FOR A PET

```bash
curl http://localhost:8080/api/owners/1/pets/1/visits
```

---

### 11. GET ALL VETS

```bash
curl http://localhost:8080/api/vets
```

---

### 12. GET ALL SPECIALTIES

```bash
curl http://localhost:8080/api/specialties
```

---

### 13. DELETE A PET

```bash
curl -X DELETE http://localhost:8080/api/owners/1/pets/1
```

**Response (204 No Content)** - No response body

---

### 14. DELETE AN OWNER

```bash
curl -X DELETE http://localhost:8080/api/owners/1
```

**Response (204 No Content)** - No response body

---

## Using Postman

### Step 1: Download and Install Postman
https://www.postman.com/downloads/

### Step 2: Create Requests

#### Request 1: GET All Owners
```
Method: GET
URL: http://localhost:8080/api/owners
Headers: (none needed for GET)
```

#### Request 2: CREATE Owner
```
Method: POST
URL: http://localhost:8080/api/owners
Headers:
  Content-Type: application/json
Body (raw JSON):
{
  "firstName": "John",
  "lastName": "Smith",
  "address": "123 Main Street",
  "city": "New York",
  "telephone": "5551234567"
}
```

#### Request 3: GET Specific Owner
```
Method: GET
URL: http://localhost:8080/api/owners/1
Headers: (none needed)
```

#### Request 4: UPDATE Owner
```
Method: PUT
URL: http://localhost:8080/api/owners/1
Headers:
  Content-Type: application/json
Body (raw JSON):
{
  "firstName": "Jane",
  "lastName": "Doe",
  "address": "456 Oak Avenue",
  "city": "Boston",
  "telephone": "6171234567"
}
```

#### Request 5: CREATE Pet
```
Method: POST
URL: http://localhost:8080/api/owners/1/pets
Headers:
  Content-Type: application/json
Body (raw JSON):
{
  "name": "Fluffy",
  "birthDate": "2020-05-15",
  "type": {
    "id": 1,
    "name": "Cat"
  }
}
```

#### Request 6: CREATE Visit
```
Method: POST
URL: http://localhost:8080/api/owners/1/pets/1/visits
Headers:
  Content-Type: application/json
Body (raw JSON):
{
  "date": "2025-02-15",
  "description": "Annual checkup"
}
```

---

## Complete Workflow Examples

### Workflow 1: Create Complete Pet Medical History

#### Step 1: Create Owner
```bash
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Sarah",
    "lastName": "Johnson",
    "address": "789 Maple Road",
    "city": "Los Angeles",
    "telephone": "3108675309"
  }'
```
Save the returned `id` (let's say it's 2)

#### Step 2: Get Pet Types
```bash
curl http://localhost:8080/api/pet-types
```
Note: Get a pet type ID (e.g., 2 for Dog)

#### Step 3: Create Pet
```bash
curl -X POST http://localhost:8080/api/owners/2/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Max",
    "birthDate": "2018-03-20",
    "type": {
      "id": 2,
      "name": "Dog"
    }
  }'
```
Save the returned pet `id` (let's say it's 2)

#### Step 4: Add First Visit
```bash
curl -X POST http://localhost:8080/api/owners/2/pets/2/visits \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2024-06-15",
    "description": "Vaccinations updated"
  }'
```

#### Step 5: Add Second Visit
```bash
curl -X POST http://localhost:8080/api/owners/2/pets/2/visits \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-01-20",
    "description": "Dental cleaning"
  }'
```

#### Step 6: View Pet's Medical History
```bash
curl http://localhost:8080/api/owners/2/pets/2/visits
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "date": "2024-06-15",
    "description": "Vaccinations updated"
  },
  {
    "id": 2,
    "date": "2025-01-20",
    "description": "Dental cleaning"
  }
]
```

---

### Workflow 2: Multiple Owners and Pets

#### Create Multiple Owners
```bash
# Owner 1
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Wilson","address":"111 Pine St","city":"Seattle","telephone":"2065551234"}'

# Owner 2
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Bob","lastName":"Brown","address":"222 Elm St","city":"Portland","telephone":"5035551234"}'

# Owner 3
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Carol","lastName":"Davis","address":"333 Oak St","city":"Denver","telephone":"3035551234"}'
```

#### Search Owners by Last Name
```bash
# Search for "Wilson"
curl "http://localhost:8080/api/owners/search?lastName=Wilson"

# Search for "Brown"
curl "http://localhost:8080/api/owners/search?lastName=Brown"
```

---

## Error Handling

### Example 1: Invalid Data (400 Bad Request)

**Invalid Telephone Format:**
```bash
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "address": "123 Main Street",
    "city": "New York",
    "telephone": "123"
  }'
```

**Response (400 Bad Request):**
```json
{
  "error": "Bad Request",
  "message": "Invalid telephone format. Must be 10 digits."
}
```

---

### Example 2: Owner Not Found (404)

```bash
curl http://localhost:8080/api/owners/999
```

**Response (404 Not Found):**
```json
{
  "error": "Not Found",
  "message": "Owner with id 999 not found"
}
```

---

### Example 3: Missing Required Field (400)

```bash
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith"
  }'
```

**Response (400 Bad Request):**
```json
{
  "error": "Bad Request",
  "message": "Address and City and Telephone are required"
}
```

---

### Example 4: Future Birth Date (400)

```bash
curl -X POST http://localhost:8080/api/owners/1/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fluffy",
    "birthDate": "2026-12-31",
    "type": {"id": 1, "name": "Cat"}
  }'
```

**Response (400 Bad Request):**
```json
{
  "error": "Bad Request",
  "message": "Birth date cannot be in the future"
}
```

---

## Tips and Tricks

### 1. Pagination

Get second page of owners:
```bash
curl "http://localhost:8080/api/owners?page=2"
```

Get third page:
```bash
curl "http://localhost:8080/api/owners?page=3"
```

---

### 2. Using Variables in cURL

Store owner ID:
```bash
OWNER_ID=$(curl -s -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Smith","address":"123 Main St","city":"NY","telephone":"5551234567"}' | jq '.id')

echo $OWNER_ID  # Output: 1
```

Then use it:
```bash
curl http://localhost:8080/api/owners/$OWNER_ID
```

---

### 3. Pretty Print JSON with jq

Install jq: https://stedolan.github.io/jq/

```bash
curl http://localhost:8080/api/owners | jq .
```

Or format specific field:
```bash
curl http://localhost:8080/api/owners | jq '.content[0].firstName'
```

---

### 4. Save Response to File

```bash
curl http://localhost:8080/api/owners > owners.json
```

---

### 5. Combine Multiple Operations

Create owner and pet in one script:
```bash
#!/bin/bash

# Create owner
OWNER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "address": "123 Main Street",
    "city": "New York",
    "telephone": "5551234567"
  }')

OWNER_ID=$(echo $OWNER_RESPONSE | jq '.id')
echo "Created owner with ID: $OWNER_ID"

# Create pet for owner
PET_RESPONSE=$(curl -s -X POST http://localhost:8080/api/owners/$OWNER_ID/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fluffy",
    "birthDate": "2020-05-15",
    "type": {"id": 1, "name": "Cat"}
  }')

PET_ID=$(echo $PET_RESPONSE | jq '.id')
echo "Created pet with ID: $PET_ID"

# Create visit for pet
curl -s -X POST http://localhost:8080/api/owners/$OWNER_ID/pets/$PET_ID/visits \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-02-15",
    "description": "Annual checkup"
  }' | jq .

echo "Visit created successfully!"
```

Save as `create_pet_record.sh` and run:
```bash
chmod +x create_pet_record.sh
./create_pet_record.sh
```

---

## API Response Summary

| Operation | Method | Endpoint | Status Code |
|-----------|--------|----------|-------------|
| List Owners | GET | /api/owners | 200 |
| Search Owners | GET | /api/owners/search?lastName=X | 200 |
| Get Owner | GET | /api/owners/{id} | 200 |
| Create Owner | POST | /api/owners | 201 |
| Update Owner | PUT | /api/owners/{id} | 200 |
| Delete Owner | DELETE | /api/owners/{id} | 204 |
| Get Pets | GET | /api/owners/{ownerId}/pets | 200 |
| Get Pet | GET | /api/owners/{ownerId}/pets/{id} | 200 |
| Create Pet | POST | /api/owners/{ownerId}/pets | 201 |
| Update Pet | PUT | /api/owners/{ownerId}/pets/{id} | 200 |
| Delete Pet | DELETE | /api/owners/{ownerId}/pets/{id} | 204 |
| Get Visits | GET | /api/owners/{ownerId}/pets/{petId}/visits | 200 |
| Create Visit | POST | /api/owners/{ownerId}/pets/{petId}/visits | 201 |
| Update Visit | PUT | /api/owners/{ownerId}/pets/{petId}/visits/{id} | 200 |
| Delete Visit | DELETE | /api/owners/{ownerId}/pets/{petId}/visits/{id} | 204 |
| Get Vets | GET | /api/vets | 200 |
| Get Pet Types | GET | /api/pet-types | 200 |
| Get Specialties | GET | /api/specialties | 200 |

---

## Quick Reference - Common Tasks

### Get all data
```bash
curl http://localhost:8080/api/owners
curl http://localhost:8080/api/vets
curl http://localhost:8080/api/pet-types
curl http://localhost:8080/api/specialties
```

### Create and manage owners
```bash
# Create
curl -X POST http://localhost:8080/api/owners -H "Content-Type: application/json" -d '{...}'

# Read
curl http://localhost:8080/api/owners/1

# Update
curl -X PUT http://localhost:8080/api/owners/1 -H "Content-Type: application/json" -d '{...}'

# Delete
curl -X DELETE http://localhost:8080/api/owners/1
```

### Manage pets
```bash
# List pets
curl http://localhost:8080/api/owners/1/pets

# Create pet
curl -X POST http://localhost:8080/api/owners/1/pets -H "Content-Type: application/json" -d '{...}'

# Update pet
curl -X PUT http://localhost:8080/api/owners/1/pets/1 -H "Content-Type: application/json" -d '{...}'

# Delete pet
curl -X DELETE http://localhost:8080/api/owners/1/pets/1
```

### Manage visits
```bash
# List visits
curl http://localhost:8080/api/owners/1/pets/1/visits

# Create visit
curl -X POST http://localhost:8080/api/owners/1/pets/1/visits -H "Content-Type: application/json" -d '{...}'

# Update visit
curl -X PUT http://localhost:8080/api/owners/1/pets/1/visits/1 -H "Content-Type: application/json" -d '{...}'

# Delete visit
curl -X DELETE http://localhost:8080/api/owners/1/pets/1/visits/1
```

---

## Conclusion

You now have a complete REST API for managing pet clinic data! Use:
- **cURL** for quick testing from command line
- **Postman** for organized API testing and development
- **Scripts** for automation and workflows

For complete API documentation, see **REST_API.md**
