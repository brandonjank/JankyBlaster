package com.brandonjank.jankyblaster;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Phelps on 5/8/2016.
 */
public class SpaceMine {

    GameScreen game;
    Sprite sprite;
    boolean free = false;
    boolean isMine = false;
    Vector2 position = new Vector2();
    String owner = "";
    String player = "";

    public void place(float x, float y) {
        sprite.setPosition(x, y);
        position.set(x, y);
        update();
    }

    public void update() {
        // lol nothing ?
    }

    public void draw(Batch batch) {
        if (isMine) {
            sprite.setTexture(Assets.item1Texture);
        }
        else {
            sprite.setTexture(Assets.item2Texture);
        }
        sprite.draw(batch);
    }


    public SpaceMine(GameScreen game) {
        this.game = game;
    }

}
