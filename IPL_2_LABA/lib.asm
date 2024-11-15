%include "lib.inc"


section .text
; Принимает код возврата и завершает текущий процесс
exit:
    mov rax,60
    syscall

; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
    xor rax, rax         ; Обнуляем счетчик длины
.length_loop:
    cmp byte [rdi + rax], 0 ; Проверяем конец строки (нуль-терминатор)
    je .done            ; Если нуль-терминатор найден, выходим
    inc rax             ; Увеличиваем счетчик
    jmp .length_loop    ; Переходим к следующему символу
.done:
    ret                 ; Возврат из функции, результат в rax
; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
    xor rax, rax
    push rdi          ; Файловый дескриптор (1 = stdout)
    call string_length  ; Подсчитываем длину строки
    pop rsi
    mov rdx, rax
    mov rdi, 1; Длина строки в rdx (передаем как аргумент для write)
    mov rax, 1          ; Код системного вызова write
    syscall             ; Вызов системного вызова
    ret                 ; Возврат из функции
; Принимает код символа и выводит его в stdout
print_char:
    push rdi
    mov rdi,rsp
    call print_string
    pop rdi
    ret
; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    xor rax, rax
    mov rsi, rdi        ; Адрес символа новой строки
    mov rdi, 1          ; Файловый дескриптор (1 = stdout)
    mov rdx, 1          ; Количество байтов (1 байт = 1 символ)
    mov rax, 1          ; Код системного вызова write
    syscall             ; Вызов системного вызова
    ret              ; Возврат из функции
; Выводит беззнаковое 8-байтовое число в десятичном формате
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
    xor rax,rax
    xor r9, r9
    mov r11, 10
    mov rax, rdi
.loop:
    xor rdx, rdx
    div r11             ;остаток от 10 в rdx
    add rdx, '0'        ;Делаем символ в аски
    dec rsp             ;уменьшаем указатель стека
    mov [rsp], dl       ;запишсь rdx 8 бит в rsp
    inc r9
    test rax, rax       ;проверка на флаги
    jnz .loop
    mov rax, 1          ;вывод
    mov rdi, 1
    mov rsi, rsp
    mov rdx, r9
    syscall
    add rsp, r9
    ret
; Выводит знаковое 8-байтовое число в десятичном формате
print_int:
    xor rax, rax
    test rdi,rdi
    jns .convert
    push rdi
    mov rdi,'-'
    call print_char
    pop rdi
    neg rdi
.convert:
    sub rsp,8
    call print_uint
    add rsp,8
    ret

; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
    xor rax, rax                ; Обнуляем rax (результат)

.next_char:
    mov al, [rdi]               ; Читаем текущий символ из первой строки
    mov dl, [rsi]               ; Читаем текущий символ из второй строки

    cmp al, dl                  ; Сравниваем символы
    jne .not_equal              ; Если не равны, переходим к .not_equal
    test al, al                 ; Проверяем, достигли ли конца строки (нулевой терминатор)
    jz .equal                   ; Если конец строки, строки равны

    ; Переход к следующему символу
    inc rdi                     ; Переходим к следующему символу в первой строке
    inc rsi                     ; Переходим к следующему символу во второй строке
    jmp .next_char              ; Повторяем цикл

.not_equal:
    xor rax,rax                   ; Устанавливаем результат в 0 (строки не равны)
    ret
.equal:
    mov rax,1
    ret


; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
    dec rsp
    xor rax, rax
    mov rsi, rsp
    mov rdi, 0
    mov rdx, 1
    syscall
    test rax, rax
    je .eof
    mov  al, [rsp]
    inc rsp
    ret
.eof:
    xor rax, rax
    inc rsp
    ret



; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор
read_word:
    xor r9, r9
.skip:
    push rdi
    push rsi
    push r9
    xor al, al
    call read_char
    pop r9
    pop rsi
    pop rdi
    cmp al, 0x20
    jz .skip
    cmp al, 0x9
    jz .skip
    cmp al, 0xA
    jz .skip
    test al, al
    jz .end
    dec rsi
.word:
    cmp r9, rsi
    je .fail
    mov byte[rdi + r9], al
    inc r9
    push rdi
    push rsi
    push r9
    xor al, al
    call read_char
    pop r9
    pop rsi
    pop rdi
    cmp al, 0x20
    jz .end
    cmp al, 0x9
    jz .end
    cmp al, 0xA
    jz .end
    test al, al
    jz .end
    jmp .word
.end:
    mov byte [rdi + r9], 0
    mov rax, rdi
    mov rdx, r9
    ret
.fail:
    xor rax, rax
    xor rdx, rdx
    ret

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось

parse_uint:
    xor r10,r10
    mov r10, 10
    xor rax, rax
    xor rcx, rcx
    xor rdx, rdx
.loop:
    movzx r9, byte [rdi + rcx]
    cmp r9b, '0'
    jb .end
    cmp r9b, '9'
    ja .end
    mul r10
    and r9b, 0x0f
    add rax, r9
    inc rcx
    jmp .loop
    .end:
    mov rdx, rcx
    ret
; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был)
; rdx = 0 если число прочитать не удалось
parse_int:
    xor rax, rax
.check_neg:
    cmp byte [rdi],'-'  ;чек на знак
    jz .parse_neg

    jmp parse_uint

.parse_neg:
    push rdi
    inc rdi
    call parse_uint ;парс функция
    inc rdx
    neg rax         ;если нег инвертируем
    pop rdi
    ret

; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
     ; Обнуляем rax (счётчик скопированных байт)
  xor rcx, rcx
.copy_loop:
  cmp rcx, rdx          ; Проверяем, не превысили ли мы размер буфера
  jge .overflow
  mov al, byte [rdi+rcx]    ; Читаем текущий символ из строки-источника (адрес в rdi)
  mov byte [rsi + rcx], al   ; Копируем текущий символ в буфер назначения (адрес в rsi)
  inc rcx               ; Увеличиваем счётчик скопированных байт
  test al, al           ; Проверка на нуль-терминатор
  jz .finish            ; Если это нуль-терминатор, переходим к завершению
              ; Переходим к следующему символу источника
  jmp .copy_loop        ; Переходим к следующей итерации цикла
.overflow:
  xor rax, rax          ; Обнуляем rax для возврата 0 в случае переполнения
  ret                   ; Возврат из функции

.finish:
  ret                   ; Возврат из функции
