# --- MessMate Backend Makefile ---

# Variables with defaults (override via command line, e.g., make docker-build TAG=v4)
IMAGE_NAME ?= devyugal1107/mess-java-backend
TAG ?= latest

# Default goal when running just 'make'
.DEFAULT_GOAL := help

# Help command to list available targets
help:
	@echo "Available commands:"
	@echo "  make run         - Run the application in PRODUCTION mode (loads .env)"
	@echo "  make dev         - Run the application in DEVELOPMENT mode (loads .env)"
	@echo "  make build       - Build the application (JAR file)"
	@echo "  make start       - Run the already BUILT JAR file (Production style)"
	@echo "  make clean       - Clean the build directory"
	@echo "  make test        - Run tests"
	@echo "  make docker-build - Build Docker image (optional: make docker-build TAG=v4)"
	@echo "  make docker-run   - Run Docker container in background (optional: make docker-run TAG=v4)"
	@echo "  make docker-stop  - Stop and remove the running Docker container"

# Run in production mode
run:
	@export $$(grep -v '^#' .env | xargs) && ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Run in development mode
dev:
	@export $$(grep -v '^#' .env | xargs) && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Build the project
build:
	./mvnw clean package -DskipTests

# Run the built JAR file directly
start:
	@export $$(grep -v '^#' .env | xargs) && java -jar target/MessMate-0.0.1-SNAPSHOT.jar

# Clean the project
clean:
	./mvnw clean

# Run tests
test:
	./mvnw test

# Build Docker image
docker-build:
	docker build -t $(IMAGE_NAME):$(TAG) .

# Stop and remove the existing container if it exists
docker-stop:
	docker stop messmate-backend || true
	docker rm messmate-backend || true

# Run Docker container in production mode (stops the old one first)
docker-run: docker-stop
	docker run -d \
		--name messmate-backend \
		--restart unless-stopped \
		-p 8080:8080 \
		--env-file .env \
		-e SPRING_PROFILES_ACTIVE=prod \
		$(IMAGE_NAME):$(TAG)
