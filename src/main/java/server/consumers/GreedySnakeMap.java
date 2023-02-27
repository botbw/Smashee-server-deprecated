package server.consumers;

import java.util.Random;

public class GreedySnakeMap { // generate symmetric map
    private static final Integer row = 13;
    private static final Integer col = 14;
    private static final Integer wall_cnt = 40;
    private static final int dx[] = {-1, 0, 1, 0}, dy[] = {0, 1, 0, -1};
    public final boolean[][] walls;

    public GreedySnakeMap() {
        this.walls = new boolean[row][col];
        int time = 0;
        final int timeout = 1000;
        while(time < timeout) {
            draw();
            if(dfs(1, 1, row - 2, col - 2)) {
                break;
            }
        }
    }

    void draw() {
        for(int i = 0; i < row; i++) {
            walls[i][0] = walls[i][col - 1] = true;
        }

        for(int j = 0; j < col; j++) {
            walls[0][j] = walls[row - 1][j] = true;
        }
        int cnt = 0;
        int time = 0;
        final int timeout = 1000;

        Random r = new Random();

        while(cnt < wall_cnt && time < timeout) {
            time++;
            int i = r.nextInt(row);
            int j = r.nextInt(col);

            if((i == 1 && j == 1) || (i == row-2 && j == col-2)) continue;
            if(walls[i][j]) continue;
            walls[i][j] = walls[row - i - 1][col - j - 1] = true;
            cnt += 2;
        }
    }

    boolean dfs(int x, int y, int dstX, int dstY) {
        if(x == dstX && y == dstY) return true;
        walls[x][y] = true;

        for(int i = 0; i < 4; i++) {
            int xx = x + dx[i], yy = y + dy[i];
            if(xx >= 0 && yy >= 0 && xx < col && yy < col && !walls[xx][yy])
                if(dfs(xx, yy, dstX, dstY)) {
                    walls[x][y] = false;
                    return true;
                }
        }
        walls[x][y] = false;
        return false;
    }

}
