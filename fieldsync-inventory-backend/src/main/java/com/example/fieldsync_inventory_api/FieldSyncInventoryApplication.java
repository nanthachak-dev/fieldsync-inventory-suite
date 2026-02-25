package com.example.fieldsync_inventory_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FieldSyncInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FieldSyncInventoryApplication.class, args);
	}

}
