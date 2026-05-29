# Product Catalog

A full-stack product catalog application built with:

- Spring Boot (Java) for the backend API
- Vue 3 + Vite for the frontend UI
- PostgreSQL / H2 for data storage
- Multipart upload support for product images

## Features

- View product list and product details
- Add and edit products
- Upload product images in JPG, JPEG, and PNG formats
- Search and filter products
- Category-based product organization
- Health check endpoint

## Project Structure

- `src/main/java` - Spring Boot backend
- `src/main/resources` - application configuration and static resources
- `src/frontend` - Vue 3 frontend
- `uploads/` - uploaded product images

## Requirements

- Java 25 (as configured in Gradle)
- Node.js 24.11.0
- Gradle
- PostgreSQL (for production profile)

## Backend Run

From the project root:

```bash
./gradlew bootRun
```

The backend will run on:

```text
http://localhost:8080
```

## Frontend Run

From the frontend folder:

```bash
cd src/frontend
npm install
npm run dev
```

The frontend will run on:

```text
http://localhost:5173
```

## API Endpoints

### Health

```http
GET /api/health
```

### Products

```http
GET /api/products
GET /api/products/{id}
POST /api/products
PUT /api/products/{id}
DELETE /api/products/{id}
```

## Image Upload

Product images support these formats:

- `.jpg`
- `.jpeg`
- `.png`

Uploaded files are stored under the `uploads/` folder and served via `/uploads/...`.

## Notes

- Development profile uses H2 in memory.
- Production profile uses PostgreSQL (`application-prod.properties`).
- The frontend is built into Spring Boot static resources during Gradle build.

## Build

From the project root:

```bash
./gradlew build
```
