package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Json;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by Phelps on 4/17/2016.
 */
public class GameInputProcessor implements InputProcessor {

    GameScreen game;
    Body body;

    public GameInputProcessor(GameScreen game) {
        this.game = game;
        body = game.player.body;
    }

    private void broadcastEvent(String event, int keycode) {
        JSONObject data = new JSONObject();
        try {
            data.put("event", event);
            data.put("keycode", keycode);
            data.put("sender", game.username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        game.warp.sendUpdatePeers(data.toString().getBytes());
    }

    @Override
    public boolean keyDown(int keycode) {
        game.player.handleKeyInput(InputEvent.Type.keyDown, keycode, false);
        //broadcastEvent("keyDown", keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        game.player.handleKeyInput(InputEvent.Type.keyUp, keycode, false);
        //broadcastEvent("keyUp", keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
