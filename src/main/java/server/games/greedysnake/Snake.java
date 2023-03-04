package server.games.greedysnake;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private static final Integer INIT_LEN = 3;
    private Integer uid;
    private Integer x;
    private Integer y;

    private Integer step;
    private List<SnakeCell> cells;
    public Direction dir;

    public Snake(Integer uid, Integer x, Integer y) {
        this.uid = uid;
        this.x = x;
        this.y = y;
        this.step = 0;
        this.cells = new ArrayList<>();
        this.dir = null;
    }

    public Integer getX() {
        return x;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getY() {
        return y;
    }

    private boolean checkLongerSnake() {
        if(this.cells.size() <= this.INIT_LEN) return true;
        else if(this.step != Snake.INIT_LEN && (this.step - this.INIT_LEN) % 5 == 0) return true;
        return false;
    }

//    void next_step() {
//        const d = this.dir;
//        const dx = Snake.dx, dy = Snake.dy;
//
//        this.next_cell = new SnakeCell(this.cells[0].x + dx[d], this.cells[0].y + dy[d]);
//        this.dir = -1;
//
//        let new_cells = [];
//        new_cells.push(new SnakeCell(this.cells[0].x, this.cells[0].y));
//        for(let cell of this.cells) {
//            new_cells.push(cell);
//        }
//        this.cells = new_cells;
//
//        this.state = Snake.StateEnum.moving;
//        this.step++;
//
//        // if(!this.map.check_valid(this.next_cell)) this.state = Snake.StateEnum.died;
//    }
}
