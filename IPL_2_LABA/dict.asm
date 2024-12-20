%include "diсt.inc"
section .text
find_word:
    push r15
    push r14
    xor r15,r15
    xor r14,r14
    mov r14,rdi
.loop
    mov r15,rsi
    add rsi,8
    mov rdi,r14
    call string_equals
    test rax,rax

    jnz .equal
    mov rsi,[r15]
    test rsi,rsi
    jnz .loop
    xor rax,rax
    jmp .end
.equal:
    mov rsi,r15
    add rsi,8
    mov rdi,r14
    call string_length
    add r15,rax
    add r15,9
    mov rax,r15
.end:
    pop r14
    pop r15
    ret

