package server.consumers;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import server.mappers.UserMapper;
import server.pojos.User;
import server.utils.JwtUtil;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{jwt}")
public class WebSocketServer { // NOT BEAN
    private static ConcurrentHashMap<Integer, WebSocketServer> userOf = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<User> pool = new CopyOnWriteArraySet<>();

    // client session
    private Session session;
    // client user
    private User user;

    // the class is not a bean
    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("jwt") String jwt) throws IOException {
        this.session = session;
        System.out.println("onOpen");
        Integer uid = JwtUtil.parseUserId(jwt);

        this.user = userMapper.selectById(uid);

        if(this.user != null) {
            userOf.put(uid, this);
        } else {
            session.close();
        }
        System.out.println(userOf);
    }

    @OnClose
    public void onClose() {
        System.out.println("onClose");
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        System.out.println("onMsg");
        JSONObject json = JSONObject.parseObject(msg);
        String event = json.getString("event");
        System.out.println(event);
        if("start".equals(event)) {
            startMatching();
        } else if("cancel".equals(event)) {
            cancelMatching();
        } else {
            sendMessage("unknown event");
        }


    }

    private void startMatching() {
        synchronized (pool) {
            pool.add(this.user);

            while(pool.size() >= 2) {
                System.out.println("dispatching");
                Iterator<User> it = pool.iterator();

                User u1 = it.next(), u2 = it.next();

                GreedySnakeMap map = new GreedySnakeMap();

                pool.remove(u1);
                pool.remove(u2);

                JSONObject resp1 = new JSONObject();
                resp1.put("event", "GameStart");
                JSONObject oppo1 = new JSONObject();
                oppo1.put("usrname", u2.getUsrname());
                oppo1.put("avatar", u2.getAvatar());
                resp1.put("opponent", oppo1);
                resp1.put("map", map.walls);
                userOf.get(u1.getUid()).sendMessage(resp1.toJSONString());

                JSONObject resp2 = new JSONObject();
                resp2.put("event", "GameStart");
                JSONObject oppo2 = new JSONObject();
                oppo2.put("usrname", u1.getUsrname());
                oppo2.put("avatar", u1.getAvatar());
                resp2.put("opponent", oppo2);
                resp2.put("map", map.walls);
                userOf.get(u2.getUid()).sendMessage(resp2.toJSONString());

            }
        }
    }

    private void cancelMatching() {

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String msg) {
        try {
            synchronized (this.session) {
                this.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
