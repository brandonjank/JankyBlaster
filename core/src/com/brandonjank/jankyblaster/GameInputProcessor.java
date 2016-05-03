/*
 * GameInputProcessor.java
 *
 * Created: 4/20/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class GameInputProcessor implements InputProcessor {

    GameScreen game;

    public GameInputProcessor(GameScreen game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        game.player.handleKeyInput(InputEvent.Type.keyDown, keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        game.player.handleKeyInput(InputEvent.Type.keyUp, keycode);
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
