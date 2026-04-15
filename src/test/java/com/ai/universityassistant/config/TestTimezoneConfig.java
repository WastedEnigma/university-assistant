package com.ai.universityassistant.config;

import java.util.TimeZone;

import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestTimezoneConfig {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
