package App.service;


import App.model.DotDTO;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static App.jooq.Tables.USERS;
import static App.jooq.tables.Dots.DOTS;

@Service
public class DotService {

    private final DSLContext dsl;

    @Autowired
    public DotService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ResponseEntity<DotDTO> addDot(double x, double y, long r, boolean isInArea, int userId) {
        try {
            boolean userExists = dsl.fetchCount(dsl.selectFrom(USERS).where(USERS.ID.eq(userId))) > 0;
            if (!userExists) {
                return ResponseEntity.badRequest().body(null);
            }

            dsl.insertInto(DOTS).set(DOTS.X, x)
                    .set(DOTS.Y, y)
                    .set(DOTS.R, r)
                    .set(DOTS.ISAREA, isInArea)
                    .set(DOTS.IDUSER, userId)
                    .execute();

            DotDTO addedDot = new DotDTO(x, y, r, isInArea);  // Сконструируйте объект на основе введенных данных
            return ResponseEntity.status(HttpStatus.CREATED).body(addedDot);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    public ResponseEntity<List<DotDTO>> getUserDots(int userId) {
        try {
            List<DotDTO> dots = dsl.selectFrom(DOTS)
                    .where(DOTS.IDUSER.eq(userId))
                    .fetch()
                    .map(record -> new DotDTO(record.getX(), record.getY(), record.getR(), record.getIsarea()));

            return ResponseEntity.ok(dots);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
}


