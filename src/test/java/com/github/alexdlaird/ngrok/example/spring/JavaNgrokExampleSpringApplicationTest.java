/*
 * Copyright (c) 2021-2024 Alex Laird
 *
 * SPDX-License-Identifier: MIT
 */

package com.github.alexdlaird.ngrok.example.spring;

import com.github.alexdlaird.ngrok.example.spring.conf.NgrokConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static com.github.alexdlaird.util.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JavaNgrokExampleSpringApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NgrokConfiguration ngrokConfiguration;

    @Test
    public void testHealthCheck() {
        assumeTrue(isNotBlank(System.getenv("NGROK_AUTHTOKEN")), "NGROK_AUTHTOKEN environment variable not set");

        final ResponseEntity<String> response = this.restTemplate.getForEntity("http://127.0.0.1:" + port + "/actuator/health", String.class);
        assertTrue("Healthcheck success", response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(ngrokConfiguration.getPublicUrl().contains("ngrok"));
    }
}