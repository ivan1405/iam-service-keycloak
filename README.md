# IAM Service

## How to start the application

### 1. Build the artifact
```
mvn clean package  
```

### 2. Build Docker Image
```
docker build -t chapter_backend/iam-service .   
```

### 3. Run Docker Image
```
docker run -p 8080:8080 chapter_backend/iam-service   
```