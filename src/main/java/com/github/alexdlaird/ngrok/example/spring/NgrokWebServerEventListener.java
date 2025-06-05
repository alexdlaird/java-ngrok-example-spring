/*
 * Copyright (c) 2021-2024 Alex Laird
 *
 * SPDX-License-Identifier: MIT
 */

package com.github.alexdlaird.ngrok.example.spring;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.example.spring.conf.NgrokConfiguration;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@Profile("dev")
@Slf4j
public class NgrokWebServerEventListener {

    @Autowired
    private NgrokConfiguration ngrokConfiguration;

    @EventListener
    public void onApplicationEvent(final WebServerInitializedEvent event) {
        // Only install and initialize ngrok if we're actually using it
        if (ngrokConfiguration.isEnabled()) {
            final JavaNgrokConfig.Builder javaNgrokConfigBuilder = new JavaNgrokConfig.Builder();
            if (nonNull(ngrokConfiguration.getBinaryPath())) {
                log.info("Using {} as the ngrok binary", ngrokConfiguration.getBinaryPath());
                javaNgrokConfigBuilder.withNgrokPath(ngrokConfiguration.getBinaryPath());
            }

            final NgrokClient ngrokClient = new NgrokClient.Builder()
                    .withJavaNgrokConfig(javaNgrokConfigBuilder.build())
                    .build();
            ngrokConfiguration.setNgrokClient(ngrokClient);

            final int port = event.getWebServer().getPort();

            final CreateTunnel createTunnel = new CreateTunnel.Builder()
                    .withAddr(port)
                    .build();
            final Tunnel tunnel = ngrokClient.connect(createTunnel);
            final String publicUrl = tunnel.getPublicUrl();

            log.info("ngrok tunnel \"{}\" -> \"http://127.0.0.1:{}\"", publicUrl, port);

            // Update any base URLs or webhooks to use the public ngrok URL
            ngrokConfiguration.setPublicUrl(publicUrl);
            initWebhooks(publicUrl);
        }
    }

    private void initWebhooks(final String publicUrl) {
        // ... Implement updates necessary so inbound traffic uses the public-facing ngrok URL
    }
}
