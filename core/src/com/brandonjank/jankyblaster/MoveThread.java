package com.brandonjank.jankyblaster;

import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phelps on 4/26/2016.
 */
class MoveThread  extends Thread {
    private String s;
    private Float x, y, r;
    Socket socket;

    public MoveThread(Socket socket, String s, float x, float y, float r)
    {
        this.socket = socket;
        this.s = s;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void run()
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("s", s);
            obj.put("x", x);
            obj.put("y", y);
            obj.put("r", r);
            socket.emit("position", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}