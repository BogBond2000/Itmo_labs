package App.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DotDTO {
    private double x;
    private double y;
    private long r;
    private boolean isInArea;

    public DotDTO(double x, double y, long r, boolean isInArea) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isInArea = isInArea;
    }
}
