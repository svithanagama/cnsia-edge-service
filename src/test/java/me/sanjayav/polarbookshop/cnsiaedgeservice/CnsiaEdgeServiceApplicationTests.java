package me.sanjayav.polarbookshop.cnsiaedgeservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
class CnsiaEdgeServiceApplicationTests {

	private static final int REDIS_PORT = 6379;

	@Container
	static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.0.2"))
			.withExposedPorts(REDIS_PORT);

	@Test
	void verifyThatSpringContextLoads() {
	}

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", redis::getHost);
		registry.add("spring.redis.port",() -> redis.getMappedPort(REDIS_PORT));
	}
}
