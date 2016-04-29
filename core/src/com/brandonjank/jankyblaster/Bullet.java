package com.brandonjank.jankyblaster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by jank6275 on 4/17/2016.
 */
public class Bullet {

    GameScreen game;
    Ship ship;
    Texture texture;
    Sprite sprite;
    float angle;
    boolean free = false;
    boolean isMine = false;
    Vector2 position = new Vector2();
    String owner = "";
    String player = "";

    public Bullet(GameScreen game) {
        this.game = game;
        texture = Assets.bulletTexture;
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
    }

    public void applyVelocity(float x, float y, float r) {
        sprite.setPosition(x-8, y-8);
        this.angle = r;
        update();
    }

    public void update() {
        float x = 3f * (float) Math.cos(angle);
        float y = 3f * (float) Math.sin(angle);
        double r = Math.atan2(x, y);
        sprite.setPosition(sprite.getX() + x, sprite.getY() + y);
        sprite.setRotation((float) Math.toDegrees(angle) - 90);
        position.set(sprite.getX(), sprite.getY());
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }

}
