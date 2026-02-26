# Busca automática do JDK no Linux 
JAVA_HOME ?= $(shell which javac >/dev/null 2>&1 && readlink -f $$(which javac) | sed "s:/bin/javac::" || echo "/usr/lib/jvm/default-java")
JNIFLAGS := -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/linux"

LIB_NAME := libmandelbrot.so
DEL := rm -f

CC = gcc
CFLAGS = $(JNIFLAGS) -fPIC -shared

all: Mandelbrot.class $(LIB_NAME)

# Geração automática de header JNI (.h)
Mandelbrot.class: Mandelbrot.java
	javac -h . Mandelbrot.java

$(LIB_NAME): mandelbrot.c Mandelbrot.class
	$(CC) $(CFLAGS) mandelbrot.c -o $(LIB_NAME)

run: all
	java -Djava.library.path=. Mandelbrot

clean:
	$(DEL) *.class $(LIB_NAME) Mandelbrot.h

