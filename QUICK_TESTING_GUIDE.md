# Spring PetClinic REST API - Quick Testing Guide

## Quick Start

### 1. Start the Application
```bash
# Using Maven
mvn spring-boot:run

# Using Gradle
gradle bootRun
```

The application will start at: `http://localhost:8080`
API base URL: `http://localhost:8080/api`

---

## Quick Test Commands (cURL)

### Create an Owner
```bash
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "address": "123 Main Street",
    "city": "New York",
    "telephone": "5551234567"
  }'
```
**Note**: Save the returned owner ID (e.g., 1) for next steps

### List All Owners
```bash
curl http://localhost:8080/api/owners
```

### Get Specific Owner
```bash
curl http://localhost:8080/api/owners/1
```

### Update Owner
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

### Search Owners by Last Name
```bash
curl "http://localhost:8080/api/owners/search?lastName=Smith"
```

### Get All Pet Types
```bash
curl http://localhost:8080/api/pet-types
```

### Create a Pet for Owner
```bash
curl -X POST http://localhost:8080/api/owners/1/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fluffy",
    "birthDate": "2020-01-15",
    "type": {
      "id": 1,
      "name": "Cat"
    }
  }'
```
**Note**: Replace `id: 1` with an actual pet type ID. Get available pet types from `/api/pet-types`

### List Pets for Owner
```bash
curl http://localhost:8080/api/owners/1/pets
```

### Create a Visit for Pet
```bash
curl -X POST http://localhost:8080/api/owners/1/pets/1/visits \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-02-15",
    "description": "Annual checkup"
  }'
```
**Note**: Replace owner ID (1) and pet ID (1) with actual IDs

### Get All Vets
```bash
curl http://localhost:8080/api/vets
```

### Get All Specialties
```bash
curl http://localhost:8080/api/specialties
```

### Delete Owner
```bash
curl -X DELETE http://localhost:8080/api/owners/1
```

---

## Testing with Postman

### Import Collection
1. Open Postman
2. Click "Import"
3. Use the following URLs for requests

### Sample Requests

**1. Create Owner**
- Method: POST
- URL: `http://localhost:8080/api/owners`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "address": "123 Main Street",
  "city": "New York",
  "telephone": "5551234567"
}
```

**2. Get All Owners**
- Method: GET
- URL: `http://localhost:8080/api/owners`

**3. Get Owner by ID**
- Method: GET
- URL: `http://localhost:8080/api/owners/1`

**4. Update Owner**
- Method: PUT
- URL: `http://localhost:8080/api/owners/1`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "address": "456 Oak Avenue",
  "city": "Boston",
  "telephone": "6171234567"
}
```

**5. Search Owners**
- Method: GET
- URL: `http://localhost:8080/api/owners/search?lastName=Smith`

**6. Create Pet**
- Method: POST
- URL: `http://localhost:8080/api/owners/1/pets`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "name": "Fluffy",
  "birthDate": "2020-01-15",
  "type": {
    "id": 1,
    "name": "Cat"
  }
}
```

**7. Create Visit**
- Method: POST
- URL: `http://localhost:8080/api/owners/1/pets/1/visits`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "date": "2025-02-15",
  "description": "Annual checkup"
}
```

**8. Delete Owner**
- Method: DELETE
- URL: `http://localhost:8080/api/owners/1`

---

## Expected Responses

### Successful Creation (201 Created)
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

### Successful Retrieval (200 OK)
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Smith",
  "address": "123 Main Street",
  "city": "New York",
  "telephone": "5551234567",
  "pets": [...]
}
```

### Paginated Response (200 OK)
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Smith",
      ...
    }
  ],
  "pageable": {...},
  "totalElements": 5,
  "totalPages": 1,
  "number": 0,
  "size": 10,
  ...
}
```

### Not Found (404)
```json
{
  "error": "Not Found",
  "message": "Owner not found with id: 999"
}
```

### Validation Error (400)
```json
{
  "error": "Bad Request",
  "message": "Validation failed",
  "details": [...]
}
```

### Successful Deletion (204 No Content)
No response body

---

## Troubleshooting

### Issue: Port 8080 Already in Use
```bash
# Use a different port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Issue: Database Connection Error
- Ensure H2 database is properly configured
- Check `application.properties` for database settings
- Verify database file permissions

### Issue: Validation Error
- Check that all required fields are provided
- Verify data types match (e.g., phone number is string)
- Ensure phone number is exactly 10 digits
- Ensure birth dates are not in the future

### Issue: Resource Not Found
- Verify the ID exists
- Check the URL path is correct
- Use `GET /api/owners` to list valid owner IDs

---

## Useful Tools

### cURL
- Built-in command-line tool for making HTTP requests
- Simple syntax and quick testing

### Postman
- Full-featured API client
- Request history and organization
- Environment variables support
- Testing and automation

### VS Code REST Client Extension
- Test APIs directly in VS Code
- File-based request organization
- Syntax highlighting

### Insomnia
- User-friendly REST client
- Request chaining
- Workflow automation

### Thunder Client (VS Code)
- Built-in REST client for VS Code
- Lightweight and easy to use

---

## API Testing Workflow

1. **Create Owner**
   - POST /api/owners with owner data
   - Note the returned owner ID

2. **Create Pet Type** (if needed)
   - POST /api/pet-types with pet type name
   - Note the returned pet type ID

3. **Create Pet**
   - POST /api/owners/{ownerId}/pets with pet data
   - Note the returned pet ID

4. **Create Visit**
   - POST /api/owners/{ownerId}/pets/{petId}/visits with visit data

5. **Retrieve Data**
   - GET endpoints to verify data was created correctly

6. **Update Data**
   - PUT endpoints to modify existing resources

7. **Delete Data**
   - DELETE endpoints to clean up test data

---

## Common Error Scenarios & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| 404 Not Found | Resource ID doesn't exist | Verify ID is correct, use GET to list available items |
| 400 Bad Request | Invalid data format | Check JSON syntax, verify all required fields present |
| 400 Bad Request | Validation error (e.g., phone) | Phone must be exactly 10 digits |
| 400 Bad Request | Future birth date | Birth date must be today or in the past |
| 409 Conflict | Duplicate resource | Resource with same unique properties already exists |
| 500 Internal Server Error | Server error | Check application logs, restart application |

---

## Performance Tips

1. **Use pagination for large datasets**
   - Add `?page=2` to limit results

2. **Use specific endpoints when possible**
   - Get single owner instead of listing all

3. **Cache responses if applicable**
   - Especially for read-only endpoints

4. **Monitor response times**
   - Use browser developer tools or Postman

---

## Next Steps

After testing the API:

1. Read `REST_API.md` for complete endpoint documentation
2. Read `REST_API_CONVERSION_SUMMARY.md` for architecture overview
3. Integrate the API with your client application
4. Consider adding authentication for production use
5. Set up monitoring and logging
6. Deploy to production environment

---

## Support

For more information, refer to:
- `REST_API.md` - Complete API documentation
- `REST_API_CONVERSION_SUMMARY.md` - Implementation overview
- Spring Boot documentation: https://spring.io/projects/spring-boot
- Spring Data JPA documentation: https://spring.io/projects/spring-data-jpa
