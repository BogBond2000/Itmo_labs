ASM=nasm
ASMFLAGS=-f elf64
LD=ld
FILES_ASM = lib.asm dict.asm main.asm
FILES_O = $(patsubst %.asm, %.o, $(FILES_ASM))

%o: %asm words.inc
	@$(ASM) -o $@ $(ASMFLAGS) $<
main: $(FILES_O)
	@$(LD) -o $@ $^
test:
	@make -s main
	python3 test.py
clean:
	@rm -f *.o main
