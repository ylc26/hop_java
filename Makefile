JAVAC ?= javac
JAVA ?= java
SOURCES := $(wildcard src/*.java)
CLASSES_DIR := build/classes

.PHONY: all run clean

all:
	mkdir -p $(CLASSES_DIR)
	$(JAVAC) -d $(CLASSES_DIR) $(SOURCES)

run: all
	$(JAVA) -cp $(CLASSES_DIR) Hop

clean:
	rm -rf build
