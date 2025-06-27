can yo# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build and Test
- Build: `./gradlew build`
- Test: `./gradlew test`
- Run single test: `./gradlew test --tests "ClassName.methodName"`
- Run application: `./gradlew bootRun`
- Clean build: `./gradlew clean build`

### Development
- Local development server runs on port 9000
- Requires Redis running on localhost:6379 for rate limiting functionality

## Architecture Overview

This is a Spring Cloud Gateway edge service (API Gateway) built with Spring Boot 3.4.4 and Java 21. It serves as the entry point for the Polar Bookshop cloud-native application.

### Key Components

**Gateway Configuration** (`application.yml`):
- Routes traffic to catalog service (port 9001) and order service (port 9002)
- Implements circuit breaker pattern with Resilience4J for both catalog and order routes
- Global retry filter with exponential backoff (3 retries, 50ms-500ms backoff)
- Redis-backed rate limiting (10 requests/second replenish, 20 burst capacity)

**Rate Limiting** (`config/RateLimiterConfig.java`):
- Uses anonymous key resolver for all requests
- Configured for Redis-based distributed rate limiting

**Fallback Endpoints** (`web/WebEndpoints.java`):
- `/catalog-fallback` GET returns empty response when catalog service is down
- `/catalog-fallback` POST returns 503 SERVICE_UNAVAILABLE

### External Dependencies
- **Catalog Service**: Expected at `${CATALOG_SERVICE_URL:http://localhost:9001}/books`
- **Order Service**: Expected at `${ORDER_SERVICE_URL:http://localhost:9002}/orders`
- **Redis**: Required for rate limiting, runs on localhost:6379

### Circuit Breaker Configuration
- **Sliding window**: 20 requests
- **Failure threshold**: 50%
- **Half-open state**: 5 permitted calls
- **Wait duration**: 15 seconds
- **Timeout**: 5 seconds

### Network Configuration
- **Connection timeout**: 2 seconds
- **Response timeout**: 5 seconds
- **Idle timeout**: 15 seconds
- **Graceful shutdown**: 15 seconds