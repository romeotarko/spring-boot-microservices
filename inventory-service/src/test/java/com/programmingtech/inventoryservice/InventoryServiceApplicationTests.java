package com.programmingtech.inventoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtech.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	InventoryRepository inventoryRepository;

	@Container
	public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.26"))
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test")
			.waitingFor(Wait.forListeningPort())
			.withEnv("MYSQL_ROOT_HOST", "%");
	@Test
	void isInStock() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory?skuCode= \"iphone_15\"")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		Assertions.assertNotNull(result);
	}

	@Test
	void isNotInStock() throws Exception {
		inventoryRepository.deleteAll();
		mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory?skuCode= \"iphone_15\"")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		Assertions.assertEquals(0,inventoryRepository.findAll().size());
	}
}
