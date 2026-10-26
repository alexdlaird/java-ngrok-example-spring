[![Build](https://img.shields.io/github/actions/workflow/status/alexdlaird/java-ngrok-example-spring/build.yml)](https://github.com/alexdlaird/java-ngrok-example-spring/actions/workflows/build.yml)
![GitHub License](https://img.shields.io/github/license/alexdlaird/java-ngrok-example-spring)

# java-ngrok Example - Spring

This is an example project that shows how to easily integrate [`java-ngrok`](https://github.com/alexdlaird/java-ngrok)
with [Spring](https://spring.io/guides/gs/spring-boot/), as [summarized in the docs](https://alexdlaird.github.io/java-ngrok/integration/#spring). It also
includes a `Dockerfile` to show how to [containerize `java-ngrok`](https://alexdlaird.github.io/java-ngrok/integration/#docker).

## Running

To run the example locally, build it, then start the server, which opens a tunnel since `ngrok.enabled` is set in
`application.properties`. The tunnel's public URL is printed when the server starts.

```sh
make build
NGROK_AUTHTOKEN=$NGROK_AUTHTOKEN java -jar build/libs/java-ngrok-example-spring-1.0.0-SNAPSHOT.jar
```

To instead run it in a container built from the `Dockerfile`, use:

```sh
make build-docker
echo "NGROK_AUTHTOKEN=$NGROK_AUTHTOKEN" > .env
make run-docker
```

To run the tests, use `make test`. Tests that open a tunnel are skipped if `NGROK_AUTHTOKEN` isn't set.
