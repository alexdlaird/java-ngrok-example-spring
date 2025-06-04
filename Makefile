.PHONY: all build clean install test

SHELL := /usr/bin/env bash
ifeq ($(OS),Windows_NT)
	ifneq (,$(findstring /cygdrive/,$(PATH)))
		GRADLE_BIN := ./gradlew
	else
		GRADLE_BIN := gradlew.bat
	endif
else
	GRADLE_BIN := ./gradlew
endif

all: test

install: build

env:
	touch .env

build:
	$(GRADLE_BIN) build -x test

clean:
	$(GRADLE_BIN) clean

test:
	$(GRADLE_BIN) test

build-docker: build
	docker build -t java-ngrok-example-spring .

docker-run: env
	docker run --env-file .env -it java-ngrok-example-spring
