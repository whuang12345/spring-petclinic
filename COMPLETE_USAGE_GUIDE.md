# 📚 Complete Guide: How to Use Spring PetClinic REST API

## 🎯 Quick Start (2 Minutes)

### Option 1: Using Browser (Simplest)
1. Start application: `mvn spring-boot:run`
2. Open browser: `http://localhost:8080/api/owners`
3. You'll see all owners in JSON format!

### Option 2: Using Postman (Easiest GUI)
1. Download: https://www.postman.com/downloads/
2. Open Postman
3. Click Import → Upload Files
4. Select: `PetClinic_API_Collection.postman_collection.json`
5. Click any request and click Send!

### Option 3: Using cURL (Command Line)
```bash
curl http://localhost:8080/api/owners
```

---

## 📖 Documentation Guide

Choose your learning style:

### 👶 "I'm a Beginner"
**Start here:** `STEP_BY_STEP_TUTORIAL.md`
- 8 hands-on tutorials
- Each takes 5 minutes
- Copy-paste examples
- Real-world workflows
- Errors explained

### 🚀 "I Want to Use It Now"
**Start here:** `HOW_TO_USE_API.md`
- Practical examples with cURL and Postman
- Common tasks reference
- Error handling
- Tips and tricks
- Complete workflows

### 🛠️ "I Want Complete Reference"
**Start here:** `REST_API.md`
- All 29 endpoints listed
- Request/response formats
- Data validation rules
- HTTP status codes
- Pagination details

### 🎨 "I Prefer Visual/GUI"
**Start here:** `POSTMAN_SETUP_GUIDE.md`
- How to import collection
- Postman features
- Visual request building
- No command line needed
- Team collaboration

### 📊 "I Want Technical Details"
**Start here:** `REST_API_CONVERSION_SUMMARY.md`
- Architecture overview
- Files created
- Technology stack
- 29 endpoints summary
- Future enhancements

---

## 📁 All Files Created

### **REST Controllers (6 files)**
```
✅ OwnerRestController.java       → /api/owners
✅ PetRestController.java          → /api/owners/{id}/pets
✅ VisitRestController.java        → /api/owners/{id}/pets/{id}/visits
✅ VetRestController.java          → /api/vets
✅ PetTypeRestController.java      → /api/pet-types
✅ SpecialtyRestController.java    → /api/specialties
```

### **Repositories (2 files)**
```
✅ PetRepository.java              → Database access for pets
✅ SpecialtyRepository.java        → Database access for specialties
```

### **Documentation (8 files)**
```
✅ HOW_TO_USE_API.md               → Comprehensive usage guide
✅ STEP_BY_STEP_TUTORIAL.md        → 8 beginner-friendly tutorials
✅ REST_API.md                     → Complete API reference
✅ QUICK_TESTING_GUIDE.md          → Quick reference card
✅ REST_API_CONVERSION_SUMMARY.md  → Technical overview
✅ REST_API_INDEX.md               → Index of all changes
✅ POSTMAN_SETUP_GUIDE.md          → Postman import guide
✅ HOW_TO_USE_API_GUIDE.md         → This file!
```

### **Postman Collection (1 file)**
```
✅ PetClinic_API_Collection.postman_collection.json
   → Ready-to-import Postman collection
   → All 29 endpoints pre-configured
   → Professional testing setup
```

---

## 🎓 Learning Paths

### Path 1: Complete Beginner
1. Read: `STEP_BY_STEP_TUTORIAL.md` (30 minutes)
2. Install: Postman
3. Follow: `POSTMAN_SETUP_GUIDE.md` (10 minutes)
4. Test: Try each endpoint
5. Reference: `HOW_TO_USE_API.md` for advanced examples

### Path 2: Command Line Developer
1. Read: `HOW_TO_USE_API.md` (cURL section)
2. Practice: Run the cURL examples
3. Reference: `QUICK_TESTING_GUIDE.md` as cheat sheet
4. Learn: `REST_API.md` for all endpoints

### Path 3: API Integration Developer
1. Read: `REST_API.md` (complete reference)
2. Read: `REST_API_CONVERSION_SUMMARY.md` (architecture)
3. Test: Use Postman collection
4. Integrate: Connect from your app

### Path 4: Team Lead / Architect
1. Read: `REST_API_CONVERSION_SUMMARY.md`
2. Review: `REST_API_INDEX.md`
3. Share: Postman collection with team
4. Plan: See "Future Enhancements" section

---

## 🔍 Find What You Need

### "How do I create an owner?"
→ See `STEP_BY_STEP_TUTORIAL.md` Tutorial 1

### "What's the endpoint to get a pet?"
→ See `REST_API.md` (search "Get Pet")

### "How do I use Postman?"
→ See `POSTMAN_SETUP_GUIDE.md`

### "What are the validation rules?"
→ See `REST_API.md` Data Validation section

### "How do I delete something?"
→ See `HOW_TO_USE_API.md` Delete examples

### "What was changed in the code?"
→ See `REST_API_CONVERSION_SUMMARY.md`

### "Show me the complete workflow"
→ See `HOW_TO_USE_API.md` Complete Workflows

### "Quick reference card"
→ See `QUICK_TESTING_GUIDE.md`

---

## 📋 API Overview

### Base URL
```
http://localhost:8080/api
```

### Total Endpoints: 29

```
Owners:        6 endpoints  (GET, POST, PUT, DELETE, SEARCH)
Pets:          6 endpoints  (GET, POST, PUT, DELETE)
Visits:        5 endpoints  (GET, POST, PUT, DELETE)
Vets:          2 endpoints  (GET)
Pet Types:     5 endpoints  (GET, POST, PUT, DELETE)
Specialties:   5 endpoints  (GET, POST, PUT, DELETE)
```

### HTTP Methods Used
- **GET**    - Retrieve data
- **POST**   - Create data
- **PUT**    - Update data
- **DELETE** - Delete data

### Status Codes
- **200** - Success (GET/PUT)
- **201** - Created (POST)
- **204** - No Content (DELETE)
- **400** - Bad Request (validation error)
- **404** - Not Found
- **500** - Server Error

---

## 🚀 Getting Started

### Step 1: Start the Application
```bash
cd C:\Eclipse\workspace\spring-petclinic
mvn spring-boot:run
```

Wait for: `Started PetClinicApplication`

### Step 2: Choose Your Testing Method

**Option A: Browser (Quickest)**
```
http://localhost:8080/api/owners
```

**Option B: Postman (Best GUI)**
- Import collection from file
- Click any request → Send

**Option C: cURL (Command Line)**
```bash
curl http://localhost:8080/api/owners
```

### Step 3: Start Testing!
See documentation files for examples.

---

## 💡 Common Tasks

### Create an Owner
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
  -d '{...updated data...}'
```

### Create Pet for Owner
```bash
curl -X POST http://localhost:8080/api/owners/1/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fluffy",
    "birthDate": "2020-05-15",
    "type": {"id": 1, "name": "Cat"}
  }'
```

### Schedule a Visit
```bash
curl -X POST http://localhost:8080/api/owners/1/pets/1/visits \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-02-15",
    "description": "Annual checkup"
  }'
```

---

## 🎯 Testing Checklist

Use this to test the API:

### Owner Operations
- [ ] List all owners
- [ ] Create a new owner
- [ ] Get specific owner
- [ ] Update owner details
- [ ] Delete owner

### Pet Operations
- [ ] Get pet types
- [ ] List pets for owner
- [ ] Create a new pet
- [ ] Update pet details
- [ ] Delete pet

### Visit Operations
- [ ] Create a visit
- [ ] List visits for pet
- [ ] Update visit details
- [ ] Delete visit

### Other Operations
- [ ] List all vets
- [ ] List all specialties

---

## 🛠️ Tools You Can Use

### Command Line
- **cURL** - Built-in on Mac/Linux
- **PowerShell** - Built-in on Windows
- **Bash** - For scripting

### GUI Applications
- **Postman** - https://www.postman.com/
- **Insomnia** - https://insomnia.rest/
- **Thunder Client** - VS Code extension

### Browser
- Just paste URL in address bar for GET requests
- See JSON response directly

### Code
- JavaScript (Fetch API)
- Python (requests library)
- Java (RestTemplate)
- Any language with HTTP support

---

## 📊 File Reference Guide

| File | Type | Best For | Time |
|------|------|----------|------|
| STEP_BY_STEP_TUTORIAL.md | Tutorial | Beginners | 30 min |
| HOW_TO_USE_API.md | Guide | Examples | 15 min |
| REST_API.md | Reference | Complete docs | 20 min |
| POSTMAN_SETUP_GUIDE.md | Setup | GUI users | 10 min |
| QUICK_TESTING_GUIDE.md | Cheat sheet | Quick lookup | 5 min |
| REST_API_CONVERSION_SUMMARY.md | Technical | Architects | 15 min |
| PetClinic_API_Collection.postman_collection.json | Collection | Postman | N/A |

---

## ✅ Troubleshooting

### Application Won't Start
```
❌ Error: Port 8080 already in use
✅ Solution: Kill existing process or use different port
```

### 404 Not Found
```
❌ Error: Owner not found with id: 999
✅ Solution: Check ID exists with GET /api/owners
```

### 400 Bad Request
```
❌ Error: Validation failed
✅ Solution: Check phone is 10 digits, date not in future
```

### Connection Refused
```
❌ Error: Failed to connect
✅ Solution: Start app with mvn spring-boot:run
```

---

## 🎁 What's Included

✅ **29 Production-Ready REST Endpoints**
✅ **6 REST Controllers**
✅ **2 New Repositories**
✅ **8 Documentation Files**
✅ **1 Postman Collection**
✅ **Full CRUD Operations**
✅ **Data Validation**
✅ **Pagination Support**
✅ **Error Handling**
✅ **100% Backward Compatible**

---

## 🔄 How to Continue

### Next Steps
1. Test all endpoints with your chosen tool
2. Read the appropriate documentation file
3. Try the example workflows
4. Integrate with your client application
5. Deploy to production (if needed)

### Future Enhancements
- Add authentication (Spring Security + JWT)
- Add API versioning (/api/v1/)
- Add Swagger/OpenAPI documentation
- Add filtering and sorting
- Add caching headers
- Add rate limiting

---

## 📞 Support Resources

### Documentation
- REST_API.md - Complete API reference
- HOW_TO_USE_API.md - Usage examples
- STEP_BY_STEP_TUTORIAL.md - Tutorials

### Official Links
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Postman: https://www.postman.com/

### Tools
- cURL documentation: https://curl.se/docs/
- Postman tutorials: https://learning.postman.com/

---

## 🎉 Conclusion

You now have a complete, production-ready REST API for the Spring PetClinic application!

**Choose your path:**
- 👶 Beginner? → `STEP_BY_STEP_TUTORIAL.md`
- 🚀 Want quick examples? → `HOW_TO_USE_API.md`
- 🛠️ Need complete reference? → `REST_API.md`
- 🎨 Prefer GUI? → `POSTMAN_SETUP_GUIDE.md`
- 📊 Need technical details? → `REST_API_CONVERSION_SUMMARY.md`

**Happy API testing!** 🚀

---

**Remember:**
- Application runs at: `http://localhost:8080`
- API base URL: `http://localhost:8080/api`
- All endpoints are documented
- Try before you build
- Ask for clarification if needed

**Enjoy!** 🎊
