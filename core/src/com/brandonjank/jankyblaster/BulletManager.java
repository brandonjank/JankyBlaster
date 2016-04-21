package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by jank6275 on 4/20/2016.
 */
public class BulletManager {

    private GameScreen game;
    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    public BulletManager(GameScreen game) {
        this.game = game;
    }

    public Bullet fireBullet(float x, float y, float r, boolean isMine) {
        for (Bullet b: bullets) {
            if (b.free) {
                b.free = false;
                b.isMine = isMine;
                b.applyVelocity(x, y, r);
                return b;
            }
        }
        Gdx.app.log("BulletManager", "Size is now " + bullets.size());
        Bullet b = new Bullet(game);
        b.isMine = isMine;
        b.applyVelocity(x, y, r);
        bullets.add(b);
        return b;
    }

    public void destroyBullet(Bullet bullet) {
        Bullet b = bullets.get(bullets.indexOf(bullet));
        b.free = true;
    }

    public void update() {
        for (Bullet b: bullets) {
            if (!b.free) {
                b.update();
            }
        }
    }

    public void draw(Batch batch) {
        for (Bullet b: bullets) {
            if (!b.free) {
                b.draw(batch);
            }
        }
    }

}
