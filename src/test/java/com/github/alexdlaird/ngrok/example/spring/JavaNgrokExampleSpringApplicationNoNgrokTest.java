/*
 * Copyright (c) 2021-2024 Alex Laird
 *
 * SPDX-License-Identifier: MIT
 */

package com.github.alexdlaird.ngrok.example.spring;

import com.github.alexdlaird.ngrok.example.spring.conf.NgrokConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = {"ngrok.enabled=false"})
public class JavaNgrokExampleSpringApplicationNoNgrokTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NgrokConfiguration ngrokConfiguration;

    @Test
    public void testHealthCheckNoNgrok() {
        final ResponseEntity<String> response = this.restTemplate.getForEntity("http://127.0.0.1:" + port + "/actuator/health", String.class);
        assertTrue("Healthcheck success", response.getStatusCode().is2xxSuccessful());
        assertNull(ngrokConfiguration.getPublicUrl());
    }
}