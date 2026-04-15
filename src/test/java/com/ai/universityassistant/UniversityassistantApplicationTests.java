package com.ai.universityassistant;

import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UniversityassistantApplicationTests {

	static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

	@Test
	void contextLoads() {
	}

}
