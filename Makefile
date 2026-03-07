# --- MessMate Backend Makefile ---

# Default goal when running just 'make'
.DEFAULT_GOAL := help

# Help command to list available targets
help:
	@echo "Available commands:"
	@echo "  make run       - Run the application in PRODUCTION mode (loads .env)"
	@echo "  make dev       - Run the application in DEVELOPMENT mode (loads .env)"
	@echo "  make build     - Build the application (JAR file)"
	@echo "  make start     - Run the already BUILT JAR file (Production style)"
	@echo "  make clean     - Clean the build directory"
	@echo "  make test      - Run tests"

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
