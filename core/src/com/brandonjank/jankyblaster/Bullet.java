/*
 * Bullet.java
 *
 * Created: 4/17/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

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
        sprite.setSize(texture.getWidth() / game.P2M, texture.getHeight() / game.P2M);
        sprite.setOriginCenter();
    }

    public void applyVelocity(float x, float y, float r) {
        sprite.setPosition(x, y);
        this.angle = r;
        update();
    }

    public void update() {
        float x = 0.5f * (float) Math.cos(angle);
        float y = 0.5f * (float) Math.sin(angle);
        sprite.setPosition(sprite.getX() + x / game.P2M, sprite.getY() + y / game.P2M);
        sprite.setRotation((float) Math.toDegrees(angle) - 90);
        position.set(sprite.getX(), sprite.getY());
    }

    public void draw(Batch batch) {
        if (isMine) {
            sprite.setTexture(Assets.bulletTexture);
        }
        else {
            sprite.setTexture(Assets.enemyBulletTexture);
        }
        sprite.draw(batch);
    }

}
