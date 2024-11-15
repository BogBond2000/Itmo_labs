all: main

lib.o: lib.asm
	nasm -o lib.o -felf64 lib.asm

dict.o: dict.asm
	nasm -o dict.o -felf64 dict.asm

main: lib.o dict.o
	nasm -o main.o -felf64 main.asm
	ld -o main main.o lib.o dict.o

test: main
	python3 test.py

.PHONY: clean
