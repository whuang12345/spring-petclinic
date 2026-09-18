# Spring PetClinic REST API Documentation

## Overview
This is a complete REST API implementation of the Spring PetClinic application. All endpoints follow RESTful conventions and use JSON for request/response payloads.

## Base URL
```
http://localhost:8080/api
```

## API Endpoints

### Owner Management

#### 1. Get All Owners (with pagination)
```
GET /api/owners?page=1
```
- **Description**: Retrieve all owners with pagination
- **Query Parameters**: 
  - `page` (optional, default=1): Page number
- **Response**: Page of owners with pagination info
- **Status**: 200 OK

#### 2. Search Owners by Last Name
```
GET /api/owners/search?lastName=Smith&page=1
```
- **Description**: Search owners by last name
- **Query Parameters**:
  - `lastName` (required): Last name to search for
  - `page` (optional, default=1): Page number
- **Response**: Page of matching owners
- **Status**: 200 OK, 404 Not Found

#### 3. Get Owner by ID
```
GET /api/owners/{ownerId}
```
- **Description**: Retrieve a specific owner
- **Path Parameters**: `ownerId` - Owner ID
- **Response**: Owner object
- **Status**: 200 OK, 404 Not Found

#### 4. Create Owner
```
POST /api/owners
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "address": "123 Main St",
  "city": "New York",
  "telephone": "5551234567"
}
```
- **Description**: Create a new owner
- **Request Body**: Owner object (all fields required)
- **Response**: Created owner with ID
- **Status**: 201 Created, 400 Bad Request

#### 5. Update Owner
```
PUT /api/owners/{ownerId}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "address": "123 Main St",
  "city": "New York",
  "telephone": "5551234567"
}
```
- **Description**: Update an existing owner
- **Path Parameters**: `ownerId` - Owner ID
- **Request Body**: Owner object
- **Response**: Updated owner
- **Status**: 200 OK, 404 Not Found, 400 Bad Request

#### 6. Delete Owner
```
DELETE /api/owners/{ownerId}
```
- **Description**: Delete an owner
- **Path Parameters**: `ownerId` - Owner ID
- **Response**: None
- **Status**: 204 No Content, 404 Not Found

---

### Pet Management

#### 1. Get All Pets for an Owner
```
GET /api/owners/{ownerId}/pets
```
- **Description**: Retrieve all pets for a specific owner
- **Path Parameters**: `ownerId` - Owner ID
- **Response**: List of pets
- **Status**: 200 OK, 404 Not Found

#### 2. Get Specific Pet
```
GET /api/pets/{petId}
```
- **Description**: Retrieve a specific pet
- **Path Parameters**: `petId` - Pet ID
- **Response**: Pet object
- **Status**: 200 OK, 404 Not Found

#### 3. Get Pet for Owner
```
GET /api/owners/{ownerId}/pets/{petId}
```
- **Description**: Retrieve a specific pet for an owner
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
- **Response**: Pet object
- **Status**: 200 OK, 404 Not Found

#### 4. Create Pet for Owner
```
POST /api/owners/{ownerId}/pets
Content-Type: application/json

{
  "name": "Fluffy",
  "birthDate": "2020-01-15",
  "type": {
    "id": 1,
    "name": "Cat"
  }
}
```
- **Description**: Create a new pet for an owner
- **Path Parameters**: `ownerId` - Owner ID
- **Request Body**: Pet object
- **Response**: Created pet
- **Status**: 201 Created, 400 Bad Request, 404 Not Found

#### 5. Update Pet
```
PUT /api/owners/{ownerId}/pets/{petId}
Content-Type: application/json

{
  "name": "Fluffy",
  "birthDate": "2020-01-15",
  "type": {
    "id": 1,
    "name": "Cat"
  }
}
```
- **Description**: Update an existing pet
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
- **Request Body**: Pet object
- **Response**: Updated pet
- **Status**: 200 OK, 400 Bad Request, 404 Not Found

#### 6. Delete Pet
```
DELETE /api/owners/{ownerId}/pets/{petId}
```
- **Description**: Delete a pet
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
- **Response**: None
- **Status**: 204 No Content, 404 Not Found

---

### Visit Management

#### 1. Get All Visits for a Pet
```
GET /api/owners/{ownerId}/pets/{petId}/visits
```
- **Description**: Retrieve all visits for a pet
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
- **Response**: Collection of visits
- **Status**: 200 OK, 404 Not Found

#### 2. Get Specific Visit
```
GET /api/owners/{ownerId}/pets/{petId}/visits/{visitId}
```
- **Description**: Retrieve a specific visit
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
  - `visitId` - Visit ID
- **Response**: Visit object
- **Status**: 200 OK, 404 Not Found

#### 3. Create Visit
```
POST /api/owners/{ownerId}/pets/{petId}/visits
Content-Type: application/json

{
  "date": "2025-01-15",
  "description": "Routine checkup"
}
```
- **Description**: Create a new visit for a pet
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
- **Request Body**: Visit object
- **Response**: Created visit
- **Status**: 201 Created, 400 Bad Request, 404 Not Found

#### 4. Update Visit
```
PUT /api/owners/{ownerId}/pets/{petId}/visits/{visitId}
Content-Type: application/json

{
  "date": "2025-01-15",
  "description": "Routine checkup"
}
```
- **Description**: Update a visit
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
  - `visitId` - Visit ID
- **Request Body**: Visit object
- **Response**: Updated visit
- **Status**: 200 OK, 400 Bad Request, 404 Not Found

#### 5. Delete Visit
```
DELETE /api/owners/{ownerId}/pets/{petId}/visits/{visitId}
```
- **Description**: Delete a visit
- **Path Parameters**: 
  - `ownerId` - Owner ID
  - `petId` - Pet ID
  - `visitId` - Visit ID
- **Response**: None
- **Status**: 204 No Content, 404 Not Found

---

### Pet Type Management

#### 1. Get All Pet Types
```
GET /api/pet-types
```
- **Description**: Retrieve all pet types
- **Response**: List of pet types
- **Status**: 200 OK

#### 2. Get Pet Type by ID
```
GET /api/pet-types/{petTypeId}
```
- **Description**: Retrieve a specific pet type
- **Path Parameters**: `petTypeId` - Pet Type ID
- **Response**: Pet type object
- **Status**: 200 OK, 404 Not Found

#### 3. Create Pet Type
```
POST /api/pet-types
Content-Type: application/json

{
  "name": "Dog"
}
```
- **Description**: Create a new pet type
- **Request Body**: Pet type object
- **Response**: Created pet type
- **Status**: 201 Created, 400 Bad Request

#### 4. Update Pet Type
```
PUT /api/pet-types/{petTypeId}
Content-Type: application/json

{
  "name": "Dog"
}
```
- **Description**: Update a pet type
- **Path Parameters**: `petTypeId` - Pet Type ID
- **Request Body**: Pet type object
- **Response**: Updated pet type
- **Status**: 200 OK, 404 Not Found, 400 Bad Request

#### 5. Delete Pet Type
```
DELETE /api/pet-types/{petTypeId}
```
- **Description**: Delete a pet type
- **Path Parameters**: `petTypeId` - Pet Type ID
- **Response**: None
- **Status**: 204 No Content, 404 Not Found

---

### Vet Management

#### 1. Get All Vets (with pagination)
```
GET /api/vets?page=1
```
- **Description**: Retrieve all vets with pagination
- **Query Parameters**: 
  - `page` (optional, default=1): Page number
- **Response**: Page of vets
- **Status**: 200 OK

#### 2. Get All Vets (as list)
```
GET /api/vets/all
```
- **Description**: Retrieve all vets as a simple list
- **Response**: Collection of vets
- **Status**: 200 OK

---

### Specialty Management

#### 1. Get All Specialties
```
GET /api/specialties
```
- **Description**: Retrieve all veterinary specialties
- **Response**: List of specialties
- **Status**: 200 OK

#### 2. Get Specialty by ID
```
GET /api/specialties/{specialtyId}
```
- **Description**: Retrieve a specific specialty
- **Path Parameters**: `specialtyId` - Specialty ID
- **Response**: Specialty object
- **Status**: 200 OK, 404 Not Found

#### 3. Create Specialty
```
POST /api/specialties
Content-Type: application/json

{
  "name": "Dentistry"
}
```
- **Description**: Create a new specialty
- **Request Body**: Specialty object
- **Response**: Created specialty
- **Status**: 201 Created, 400 Bad Request

#### 4. Update Specialty
```
PUT /api/specialties/{specialtyId}
Content-Type: application/json

{
  "name": "Dentistry"
}
```
- **Description**: Update a specialty
- **Path Parameters**: `specialtyId` - Specialty ID
- **Request Body**: Specialty object
- **Response**: Updated specialty
- **Status**: 200 OK, 404 Not Found, 400 Bad Request

#### 5. Delete Specialty
```
DELETE /api/specialties/{specialtyId}
```
- **Description**: Delete a specialty
- **Path Parameters**: `specialtyId` - Specialty ID
- **Response**: None
- **Status**: 204 No Content, 404 Not Found

---

## Error Handling

All endpoints return appropriate HTTP status codes and error messages:

- **200 OK**: Successful GET or PUT request
- **201 Created**: Successful POST request
- **204 No Content**: Successful DELETE request
- **400 Bad Request**: Invalid request data or validation error
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

Error responses include a message describing the issue.

---

## Data Validation

All POST and PUT requests are validated according to entity constraints:

- **Owner**: firstName, lastName, address, city, and telephone are required
  - Telephone must be exactly 10 digits
- **Pet**: name is required; birthDate must not be in the future
- **Visit**: description is required; date must not be in the future
- **PetType**: name is required
- **Specialty**: name is required

---

## Pagination

List endpoints support pagination with the following format:
- Default page size: 10 items per page
- Page numbers are 1-indexed
- Response includes:
  - `content`: Array of items
  - `totalElements`: Total number of items
  - `totalPages`: Total number of pages
  - `number`: Current page number

---

## Testing the API

### Using cURL

Example: Get all owners
```bash
curl -X GET http://localhost:8080/api/owners
```

Example: Create an owner
```bash
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "address": "123 Main St",
    "city": "New York",
    "telephone": "5551234567"
  }'
```

Example: Update an owner
```bash
curl -X PUT http://localhost:8080/api/owners/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "address": "456 Oak Ave",
    "city": "Boston",
    "telephone": "6171234567"
  }'
```

### Using Postman

1. Import the API base URL: `http://localhost:8080/api`
2. Create requests for each endpoint
3. Use the examples provided in this documentation

---

## Implementation Notes

- All REST controllers use Spring's `@RestController` annotation
- All endpoints return `ResponseEntity` for flexible HTTP response handling
- Input validation uses Jakarta validation annotations
- Repositories use Spring Data JPA for database operations
- The API maintains transactional consistency when saving data

---

## Future Enhancements

Potential improvements for a production environment:
- Add authentication and authorization (e.g., OAuth 2.0)
- Add API versioning (e.g., `/api/v1/`)
- Add comprehensive error handling with error codes
- Add API rate limiting
- Add request/response logging
- Add Swagger/OpenAPI documentation
- Add CORS configuration if needed
- Add caching headers for GET requests
- Add custom exception handlers with detailed error responses
