%include "dict.inc"
section .text
find_word:
    push r15
    push r14
    xor r14, r14
    xor r15, r15
    mov r14, rdi ; Сохраняем указатель на искомое слово
.loop:
    mov r15, rsi ; Сохраняем текущий адрес ключа
    add rsi, 8   ; Переходим к адресу значения
    mov rdi, r14 ; Восстанавливаем указатель на искомое слово
    call string_equals
    test rax, rax
    jnz  .equal ; Если ключи равны, переходим к .equal
    mov rsi, [r15] ; Загружаем следующий ключ
    test rsi, rsi
    jnz .loop ; Если следующий ключ не равен 0, продолжаем поиск
    xor rax, rax ; Ключ не найден, возвращаем 0
    jmp .end
.equal:
    add r15, 8 ; Переходим к адресу значения
    mov rax, r15 ; Возвращаем адрес значения
.end:
    pop r14
    pop r15
    ret
