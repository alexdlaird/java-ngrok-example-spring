.PHONY: all build clean install test

SHELL := /usr/bin/env bash

all: build

build:
	$(GRADLE_BIN) build

clean:
	$(GRADLE_BIN) clean

install:
	$(GRADLE_BIN) install

test:
	$(GRADLE_BIN) test
