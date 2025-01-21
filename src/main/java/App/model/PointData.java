package App.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PointData {
    private double x;
    private double y;
    private long r;
    private boolean isInArea;
    private int userId;

    @Override
    public String toString() {
        return "App.model.PointData{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", isInArea=" + isInArea +
                ", userId=" + userId +
                '}';
    }
}