package App.service;

import App.jooq.tables.Users;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final DSLContext dsl;

    @Autowired
    public UserService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ResponseEntity<Map<String, Object>> addUser(String name, String password, PasswordEncoder passwordEncoder) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Имя пользователя не может быть пустым"));
        }
        if (password == null || password.isBlank() || password.length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Пароль должен быть не менее 6 символов"));
        }
        name = name.toLowerCase(); // Приведение к единому регистру (опционально)

        Optional<Integer> userExists = dsl.select(Users.USERS.ID)
                .from(Users.USERS)
                .where(Users.USERS.NAME.eq(name))
                .fetchOptionalInto(Integer.class);

        if (userExists.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Аккаунт с таким именем уже существует"));
        }

        try {
            String encodedPassword = passwordEncoder.encode(password);
            int userId = dsl.insertInto(Users.USERS)
                    .set(Users.USERS.NAME, name)
                    .set(Users.USERS.PASSWORD, encodedPassword)
                    .returning(Users.USERS.ID)
                    .fetchOne()
                    .getValue(Users.USERS.ID);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Пользователь успешно зарегистрирован", "userId", userId));
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace(); // Лог исключения
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Аккаунт с таким именем уже существует"));
        }
    }

    public ResponseEntity<Map<String, Object>> authenticateUser(String name, String password, PasswordEncoder passwordEncoder) {
        var userRecord = dsl.selectFrom(Users.USERS)
                .where(Users.USERS.NAME.equalIgnoreCase(name))
                .fetchOne();

        if (userRecord != null) {
            String encodedPassword = userRecord.getValue(Users.USERS.PASSWORD);
            if (passwordEncoder.matches(password, encodedPassword)) {
                int userId = userRecord.getValue(Users.USERS.ID);
                return ResponseEntity.ok(Map.of(
                        "message", "Успешная авторизация",
                        "userId", userId
                ));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Неверный логин или пароль"));
    }
}
