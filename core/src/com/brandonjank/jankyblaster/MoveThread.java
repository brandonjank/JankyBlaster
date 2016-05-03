/*
 * MoveThread.java
 *
 * Created: 4/26/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

class MoveThread  extends Thread {
    private String s;
    private Float x, y, r, e;
    Socket socket;

    public MoveThread(Socket socket, String s, float x, float y, float r, float e)
    {
        this.socket = socket;
        this.s = s;
        this.x = x;
        this.y = y;
        this.r = r;
        this.e = e;
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
            obj.put("e", e);
            socket.emit("position", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}