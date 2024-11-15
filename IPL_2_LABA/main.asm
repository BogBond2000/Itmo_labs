global _start
%include "main.inc"
%include "word.inc"
%include "colon.inc"
%include "lib.inc"



%define buffer_size 255

section .data
    message db "Phrase can not in list", 0

section .bss
    buffer resb buffer_size + 1

section .text
    global _start
    extern read_word, find_word, print_string

_start:
    xor rax,rax
    mov rdi,buffer
    mov rsi,buffer_size
    call read_word

    test rax,rax
    jz .error

    mov rdi,rax
    mov rsi, point
    call find_word ;ошибка тут вылезает

    test rax,rax
    jz .error

    add rax,8
    call print_string
    ret

.error:
    mov rdi,message
    call string_length
    mov rsi,message
    mov rdx,rax
    mov rax,1
    mov rdi,2
    syscall
    call exit