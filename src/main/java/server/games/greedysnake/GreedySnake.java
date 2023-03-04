package server.games.greedysnake;

import com.alibaba.fastjson.JSONObject;
import server.consumers.WebSocketServer;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class GreedySnake extends Thread{ // generate symmetric map
    private static final Integer[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    private static final Integer ROW_CNT = 13;
    private static final Integer COL_CNT = 14;
    private static final Integer WALL_CNT = 40;

    private boolean[][] gameMap;
    private final WebSocketServer socket1, socket2;

    private final ReentrantLock lock;
    private final Snake snake1, snake2;


    private Status status;


    public GreedySnake(WebSocketServer socket1, WebSocketServer socket2) {
        int time = 0;
        final int timeout = 1000;
        while (time < timeout) {
            this.gameMap = draw();
            if (dfs(this.gameMap,1, 1, ROW_CNT - 2, COL_CNT - 2)) {
                break;
            }
            time++;
        }
        this.socket1 = socket1;
        this.socket2 = socket2;
        Integer uid1 = socket1.getUser().getUid(), uid2 = socket2.getUser().getUid();
        this.snake1 = new Snake(uid1, 1, 1);
        this.snake2 = new Snake(uid2, COL_CNT - 2, ROW_CNT - 2);
        this.lock = new ReentrantLock();
        this.status = Status.IN_GAME;
    }

    boolean[][] draw() {
        boolean[][] map = new boolean[ROW_CNT][COL_CNT];

        for (int i = 0; i < ROW_CNT; i++) {
            map[i][0] = map[i][COL_CNT - 1] = true;
        }

        for (int j = 0; j < COL_CNT; j++) {
            map[0][j] = map[ROW_CNT - 1][j] = true;
        }
        int cnt = 0;
        int time = 0;
        final int timeout = 1000;

        Random r = new Random();

        while (cnt < WALL_CNT && time < timeout) {
            time++;
            int i = r.nextInt(ROW_CNT);
            int j = r.nextInt(COL_CNT);

            if ((i == 1 && j == 1) || (i == ROW_CNT - 2 && j == COL_CNT - 2)) continue;
            if (map[i][j]) continue;
            map[i][j] = map[ROW_CNT - i - 1][COL_CNT - j - 1] = true;
            cnt += 2;
        }
        return map;
    }

    boolean dfs(boolean[][] map, int x, int y, int dstX, int dstY) {
        if (x == dstX && y == dstY) return true;
        map[x][y] = true;

        for (int i = 0; i < 4; i++) {
            int xx = x + dx[i], yy = y + dy[i];
            if (xx >= 0 && yy >= 0 && xx < COL_CNT && yy < COL_CNT && !map[xx][yy])
                if (dfs(map, xx, yy, dstX, dstY)) {
                    map[x][y] = false;
                    return true;
                }
        }
        map[x][y] = false;
        return false;
    }

    @Override
    public void run() {
        for(int i = 0; i < 1000; i++) { // long loop
            System.out.println(i);
            if(nextStep()) { // got next step input for both
                this.status = judge();
                System.out.println(this.status.toString());
                if(this.status == Status.IN_GAME) {
                    broadcastMove();
                } else { // game ends
                    broadcastResult();
                    break;
                }
            } else { // game ends
                lock.lock();
                try {
                    if(snake1.dir == null && snake2.dir == null) {
                        status = Status.DRAW;
                    } else if(snake1.dir == null) {
                        status = Status.SNAKE2_WIN;
                    } else if(snake2.dir == null) {
                        status = Status.SNAKE1_WIN;
                    }
                } finally {
                    lock.unlock();
                }
                System.out.println("??");

                broadcastResult();
                break;
            }
        }
    }

    private boolean nextStep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100); // 100 * 100 sleep
                lock.lock();
                try {
                    if(snake1.dir != null && snake2.dir != null) return true;
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void broadcastMsg(String msg) {
        socket1.sendMessage(msg);
        socket2.sendMessage(msg);
    }

    private void broadcastResult() {
        JSONObject resp = new JSONObject();

        resp.put("event", Event.END);
        resp.put("status", status);

        broadcastMsg(resp.toJSONString());
    }

    private void broadcastMove() {
        JSONObject resp = new JSONObject();

        resp.put("event", Event.MOVE);

        lock.lock();
        try {
            resp.put("player1", snake1.dir);
            resp.put("player2", snake2.dir);
            snake1.dir = snake2.dir = null;
        } finally {
            lock.unlock();
        }
        broadcastMsg(resp.toJSONString());
    }

    private Status judge() {
        return Status.IN_GAME;
    }


    public boolean[][] getGameMap() {
        return gameMap;
    }

    public Snake getSnake1() {
        return snake1;
    }

    public Snake getSnake2() {
        return snake2;
    }

}
