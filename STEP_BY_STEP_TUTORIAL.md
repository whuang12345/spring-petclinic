# Step-by-Step Tutorial: Using PetClinic REST API

## Tutorial 1: Create Your First Pet Owner (5 minutes)

### What We'll Do
1. Start the application
2. Create a new pet owner
3. View the created owner

### Step 1: Start Application
Open PowerShell/Command Prompt and run:
```bash
cd C:\Eclipse\workspace\spring-petclinic
mvn spring-boot:run
```

Wait for the message: `Started PetClinicApplication`

### Step 2: Create Owner via cURL

Open a new PowerShell/Command Prompt window and run:

```bash
curl -X POST http://localhost:8080/api/owners `
  -H "Content-Type: application/json" `
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "address": "123 Main Street",
    "city": "New York",
    "telephone": "5551234567"
  }'
```

### Expected Output:
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

### Step 3: View Owner
Run this in your browser or cURL:
```
http://localhost:8080/api/owners/1
```

**Congratulations!** You created your first owner! ✅

---

## Tutorial 2: Add a Pet to the Owner (5 minutes)

### What We'll Do
1. Get available pet types
2. Create a pet for the owner
3. View the pet

### Step 1: Get Available Pet Types

```bash
curl http://localhost:8080/api/pet-types
```

### Expected Output:
```json
[
  {"id": 1, "name": "Cat"},
  {"id": 2, "name": "Dog"},
  {"id": 3, "name": "Hamster"},
  {"id": 4, "name": "Reptile"},
  {"id": 5, "name": "Bird"},
  {"id": 6, "name": "Rabbit"}
]
```

### Step 2: Create a Pet

Choose a pet type (let's use Cat with id=1):

```bash
curl -X POST http://localhost:8080/api/owners/1/pets `
  -H "Content-Type: application/json" `
  -d '{
    "name": "Fluffy",
    "birthDate": "2020-05-15",
    "type": {
      "id": 1,
      "name": "Cat"
    }
  }'
```

### Expected Output:
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

### Step 3: View Owner's Pets

```bash
curl http://localhost:8080/api/owners/1/pets
```

You should see your pet "Fluffy" in the list!

**Congratulations!** You added a pet! ✅

---

## Tutorial 3: Schedule a Vet Visit (5 minutes)

### What We'll Do
1. Create a visit for the pet
2. Add multiple visits
3. View visit history

### Step 1: Create First Visit

```bash
curl -X POST http://localhost:8080/api/owners/1/pets/1/visits `
  -H "Content-Type: application/json" `
  -d '{
    "date": "2025-02-15",
    "description": "Annual checkup and vaccinations"
  }'
```

### Expected Output:
```json
{
  "id": 1,
  "date": "2025-02-15",
  "description": "Annual checkup and vaccinations"
}
```

### Step 2: Add Another Visit

```bash
curl -X POST http://localhost:8080/api/owners/1/pets/1/visits `
  -H "Content-Type: application/json" `
  -d '{
    "date": "2025-03-20",
    "description": "Follow-up dental cleaning"
  }'
```

### Step 3: View All Visits

```bash
curl http://localhost:8080/api/owners/1/pets/1/visits
```

### Expected Output:
```json
[
  {
    "id": 1,
    "date": "2025-02-15",
    "description": "Annual checkup and vaccinations"
  },
  {
    "id": 2,
    "date": "2025-03-20",
    "description": "Follow-up dental cleaning"
  }
]
```

**Congratulations!** You created a visit history! ✅

---

## Tutorial 4: Update Owner Information (5 minutes)

### What We'll Do
1. Update owner's address and phone
2. Verify the changes

### Step 1: Update Owner

Change the address and phone for owner #1:

```bash
curl -X PUT http://localhost:8080/api/owners/1 `
  -H "Content-Type: application/json" `
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "address": "456 Park Avenue",
    "city": "Los Angeles",
    "telephone": "3108675309"
  }'
```

### Expected Output:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Smith",
  "address": "456 Park Avenue",
  "city": "Los Angeles",
  "telephone": "3108675309",
  "pets": [...]
}
```

### Step 2: Verify Changes

```bash
curl http://localhost:8080/api/owners/1
```

Notice the address and phone have been updated!

**Congratulations!** You updated owner information! ✅

---

## Tutorial 5: Search for Owners (5 minutes)

### What We'll Do
1. Create multiple owners
2. Search by last name
3. See pagination

### Step 1: Create More Owners

```bash
# Create Owner 2
curl -X POST http://localhost:8080/api/owners `
  -H "Content-Type: application/json" `
  -d '{"firstName":"Jane","lastName":"Smith","address":"789 Oak St","city":"Chicago","telephone":"3125551234"}'

# Create Owner 3
curl -X POST http://localhost:8080/api/owners `
  -H "Content-Type: application/json" `
  -d '{"firstName":"Bob","lastName":"Johnson","address":"321 Pine St","city":"Houston","telephone":"7135551234"}'

# Create Owner 4
curl -X POST http://localhost:8080/api/owners `
  -H "Content-Type: application/json" `
  -d '{"firstName":"Alice","lastName":"Smith","address":"654 Elm St","city":"Phoenix","telephone":"6025551234"}'
```

### Step 2: Search for "Smith"

```bash
curl "http://localhost:8080/api/owners/search?lastName=Smith"
```

### Expected Output:
```json
{
  "content": [
    {"id": 1, "firstName": "John", "lastName": "Smith", ...},
    {"id": 2, "firstName": "Jane", "lastName": "Smith", ...},
    {"id": 4, "firstName": "Alice", "lastName": "Smith", ...}
  ],
  "totalElements": 3,
  "totalPages": 1,
  "number": 0
}
```

You found all owners with last name "Smith"!

**Congratulations!** You searched for owners! ✅

---

## Tutorial 6: Delete a Visit (5 minutes)

### What We'll Do
1. View visits
2. Delete a specific visit
3. Verify deletion

### Step 1: View Visits

```bash
curl http://localhost:8080/api/owners/1/pets/1/visits
```

### Step 2: Delete Visit #2

```bash
curl -X DELETE http://localhost:8080/api/owners/1/pets/1/visits/2
```

**Expected:** No output, HTTP 204 (success)

### Step 3: Verify Deletion

```bash
curl http://localhost:8080/api/owners/1/pets/1/visits
```

Visit #2 is gone! Only visit #1 remains.

**Congratulations!** You deleted a visit! ✅

---

## Tutorial 7: Using Postman (GUI Interface)

If you prefer a graphical interface instead of command line:

### Step 1: Download Postman
- Go to: https://www.postman.com/downloads/
- Download and install

### Step 2: Create Request

In Postman:
1. Click "+" to create new request
2. Set Method to **GET**
3. Set URL to: `http://localhost:8080/api/owners`
4. Click **Send**

You should see all owners in JSON format!

### Step 3: Create Owner in Postman

1. Click "+" for new request
2. Set Method to **POST**
3. Set URL to: `http://localhost:8080/api/owners`
4. Click **Headers** and add:
   - Key: `Content-Type`
   - Value: `application/json`
5. Click **Body** → **raw** → **JSON**
6. Paste:
   ```json
   {
     "firstName": "Sarah",
     "lastName": "Williams",
     "address": "789 Birch St",
     "city": "Seattle",
     "telephone": "2065551234"
   }
   ```
7. Click **Send**

Much easier with the GUI! 

**Congratulations!** You used Postman! ✅

---

## Tutorial 8: Complete Real-World Workflow

### Scenario: Register a new client with their pet

### Step 1: Create New Owner
```bash
curl -X POST http://localhost:8080/api/owners `
  -H "Content-Type: application/json" `
  -d '{
    "firstName": "Michael",
    "lastName": "Johnson",
    "address": "999 Main Boulevard",
    "city": "San Francisco",
    "telephone": "4155551234"
  }'
```
Note the returned ID (let's say: 5)

### Step 2: Get Pet Types
```bash
curl http://localhost:8080/api/pet-types
```
Choose Dog (id=2)

### Step 3: Create Dog
```bash
curl -X POST http://localhost:8080/api/owners/5/pets `
  -H "Content-Type: application/json" `
  -d '{
    "name": "Rex",
    "birthDate": "2019-06-10",
    "type": {"id": 2, "name": "Dog"}
  }'
```
Note the pet ID (let's say: 3)

### Step 4: Schedule First Visit
```bash
curl -X POST http://localhost:8080/api/owners/5/pets/3/visits `
  -H "Content-Type: application/json" `
  -d '{
    "date": "2025-03-01",
    "description": "Initial examination and health assessment"
  }'
```

### Step 5: Schedule Follow-up
```bash
curl -X POST http://localhost:8080/api/owners/5/pets/3/visits `
  -H "Content-Type: application/json" `
  -d '{
    "date": "2025-03-15",
    "description": "Vaccination and microchip installation"
  }'
```

### Step 6: View Complete Record
```bash
curl http://localhost:8080/api/owners/5
```

You now have:
- ✅ Owner: Michael Johnson
- ✅ Pet: Rex (Dog)
- ✅ 2 Scheduled Visits

**Congratulations!** You completed a real-world workflow! ✅

---

## Common Mistakes & Solutions

### ❌ Problem: 404 Not Found
```
"error": "Owner not found with id: 999"
```
**Solution:** Check the ID exists. Use `GET /api/owners` to list valid IDs.

---

### ❌ Problem: 400 Bad Request (Telephone)
```
"message": "Telephone must be exactly 10 digits"
```
**Solution:** Provide exactly 10 digit phone number: `5551234567`

---

### ❌ Problem: 400 Bad Request (Future Date)
```
"message": "Birth date cannot be in the future"
```
**Solution:** Use a past date: `2020-05-15` (not `2026-05-15`)

---

### ❌ Problem: Connection Refused
```
curl: (7) Failed to connect
```
**Solution:** Make sure application is running with `mvn spring-boot:run`

---

### ❌ Problem: JSON Syntax Error
```
curl: (6) Couldn't resolve host name
```
**Solution:** Check JSON formatting. Use online JSON validator.

---

## Keyboard Shortcuts & Tips

### For cURL:
- Press **Up Arrow** to repeat last command
- Use backticks for multi-line commands (PowerShell)
- Pipe to `jq` for pretty printing: `curl ... | jq .`

### For Postman:
- Ctrl+S to save request
- Tab between fields
- Use environment variables for base URL

---

## Next Steps

Now that you understand how to use the API:

1. **Read REST_API.md** - Learn all available endpoints
2. **Try more operations** - Create, update, delete operations
3. **Build a client app** - Use the API from JavaScript, Python, etc.
4. **Add authentication** - Secure your API with Spring Security

---

## Quick Reference Card

```
BASE URL: http://localhost:8080/api

OWNERS:
  GET    /owners              - List all owners
  GET    /owners/search       - Search by last name
  GET    /owners/{id}         - Get owner details
  POST   /owners              - Create owner
  PUT    /owners/{id}         - Update owner
  DELETE /owners/{id}         - Delete owner

PETS:
  GET    /owners/{ownerId}/pets           - List pets
  GET    /owners/{ownerId}/pets/{petId}   - Get pet
  POST   /owners/{ownerId}/pets           - Create pet
  PUT    /owners/{ownerId}/pets/{petId}   - Update pet
  DELETE /owners/{ownerId}/pets/{petId}   - Delete pet

VISITS:
  GET    /owners/{ownerId}/pets/{petId}/visits         - List visits
  POST   /owners/{ownerId}/pets/{petId}/visits         - Create visit
  PUT    /owners/{ownerId}/pets/{petId}/visits/{id}    - Update visit
  DELETE /owners/{ownerId}/pets/{petId}/visits/{id}    - Delete visit

OTHER:
  GET /vets         - List all vets
  GET /pet-types    - List all pet types
  GET /specialties  - List all specialties
```

---

Great job! You now know how to use the PetClinic REST API! 🎉
