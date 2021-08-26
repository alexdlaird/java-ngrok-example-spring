[![CI/CD](https://github.com/alexdlaird/java-ngrok-example-spring/workflows/CI/CD/badge.svg)](https://github.com/alexdlaird/java-ngrok-example-spring/actions?query=workflow%3ACI%2FCD)
![GitHub License](https://img.shields.io/github/license/alexdlaird/java-ngrok-example-spring)

# java-ngrok Example - Spring

This is an example project that shows how to easily integrate [`java-ngrok`](https://github.com/alexdlaird/java-ngrok)
with [Spring](https://spring.io/guides/gs/spring-boot/).

## Configuration

Create a [`NgrokConfiguration`]()
class that lets us use the config to enable `ngrok` and pass it some useful parameters.

```java

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "ngrok")
public class NgrokConfiguration {
    private boolean enabled;

    private String authToken;

    private String region;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
```

And pass parameters to our Spring application through
[our config file]():

```yaml
spring.profiles.active=dev
ngrok.enabled=true
```

## Application Integration

If `ngrok.enabled` config flag is set, we want to initialize `java-ngrok` when Spring is booting. An easy way to do
this is by creating a `Component` with an `EventListener` that is executed when `WebServerInitializedEvent` is emitted.

```java
@Component
@Profile("dev")
public class NgrokWebServerEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NgrokWebServerEventListener.class);

    private final Environment environment;

    private final NgrokConfiguration ngrokConfiguration;

    @Autowired
    public NgrokWebServerEventListener(final Environment environment,
                                       final NgrokConfiguration ngrokConfiguration) {
        this.environment = environment;
        this.ngrokConfiguration = ngrokConfiguration;
    }

    @EventListener
    public void onApplicationEvent(final WebServerInitializedEvent event) {
        // java-ngrok will only be installed, and should only ever be initialized, in a dev environment
        if (ngrokConfiguration.isEnabled()) {
            final JavaNgrokConfig javaNgrokConfig = new JavaNgrokConfig.Builder()
                    .withAuthToken(ngrokConfiguration.getAuthToken())
                    .withRegion(nonNull(ngrokConfiguration.getRegion()) ? Region.valueOf(ngrokConfiguration.getRegion().toUpperCase()) : null)
                    .build();
            final NgrokClient ngrokClient = new NgrokClient.Builder()
                    .withJavaNgrokConfig(javaNgrokConfig)
                    .build();

            final int port = Integer.parseInt(environment.getProperty("server.port", "8080"));

            final CreateTunnel createTunnel = new CreateTunnel.Builder()
                    .withAddr(port)
                    .build();
            final Tunnel tunnel = ngrokClient.connect(createTunnel);

            LOGGER.info(String.format("ngrok tunnel \"%s\" -> \"http://127.0.0.1:%d\"", tunnel.getPublicUrl(), port));

            // Update any base URLs or webhooks to use the public ngrok URL
            initWebhooks(tunnel.getPublicUrl());
        }
    }

    private void initWebhooks(final String publicUrl) {
        // Update inbound traffic via APIs to use the public-facing ngrok URL
    }
}
```

Now Spring can be started by the usual means, setting `ngrok.enabled` in the config to open a tunnel.

1. Run `make install` to build the application
1. Start application with `java -jar build/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar server config.yml`
1. Check the logs for the `ngrok` tunnel's public URL, which should tunnel to  `http://localhost:8080`
