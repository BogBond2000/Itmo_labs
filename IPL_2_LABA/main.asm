global _start
%include "main.inc"
%include "words.inc"
section .data
   error_msg db "Phrase can not in list",0
section .bss
    buffer resb 256
section .text
_start:
    mov rdi, 1
    mov rax, 0
    lea rsi, 255
    mov rdx, SIZE
    syscall
    mov byte[buffer+rax-1], 0
    mov rdi, buffer
    mov rsi, point
    call find_word
    test rax, rax
    jz .error
    mov rdi, rax
    call print_string
    call exit
.error:
    mov rdi, error_msg
    call string_length
    mov rsi, error_msg
    mov rdx, rax
    mov rax, WRITE
    mov rdi, STD_ERR
    syscall
    call exit
