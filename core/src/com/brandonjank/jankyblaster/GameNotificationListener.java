package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.*;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Phelps on 4/17/2016.
 */
public class GameNotificationListener implements NotifyListener {

    GameScreen game;

    public GameNotificationListener(GameScreen game) {
        this.game = game;
    }

    @Override
    public void onRoomCreated(RoomData roomData) {

    }

    @Override
    public void onRoomDestroyed(RoomData roomData) {

    }

    @Override
    public void onUserLeftRoom(RoomData roomData, String s) {

    }

    @Override
    public void onUserJoinedRoom(RoomData roomData, String s) {

    }

    @Override
    public void onUserLeftLobby(LobbyData lobbyData, String s) {

    }

    @Override
    public void onUserJoinedLobby(LobbyData lobbyData, String s) {
        System.out.println("Joined! " + s);
        game.newPlayer(s);
    }

    @Override
    public void onChatReceived(ChatEvent chatEvent) {

    }

    @Override
    public void onPrivateChatReceived(String s, String s1) {

    }

    @Override
    public void onPrivateUpdateReceived(String s, byte[] bytes, boolean b) {

    }

    @Override
    public void onUpdatePeersReceived(UpdateEvent updateEvent) {
        try {
            JSONObject data = new JSONObject(new String(updateEvent.getUpdate()));
            try {
                if (data.has("t") && data.has("x") && data.has("y") && data.has("r")&& data.has("p")) {
                    String player = data.getString("p");
                    if (!game.username.equals(player)) {
                        Float x = (float) data.getDouble("x");
                        Float y = (float) data.getDouble("y");
                        Double r = data.getDouble("r");


                        if (data.getString("t").equals("s")) {
                            game.updatePlayer(player, x, y, r);
                        }
                        else if (data.getString("t").equals("b")){
                            System.out.println("Bullet pew ?");
                            Bullet b = new Bullet(game, x, y, r);
                            game.playerBullets.add(b);
                        }


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserChangeRoomProperty(RoomData roomData, String s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap1) {

    }

    @Override
    public void onMoveCompleted(MoveEvent moveEvent) {
        System.out.println(moveEvent.getMoveData());
    }

    @Override
    public void onGameStarted(String s, String s1, String s2) {

    }

    @Override
    public void onGameStopped(String s, String s1) {

    }

    @Override
    public void onUserPaused(String s, boolean b, String s1) {

    }

    @Override
    public void onUserResumed(String s, boolean b, String s1) {

    }

    @Override
    public void onNextTurnRequest(String s) {

    }
}
