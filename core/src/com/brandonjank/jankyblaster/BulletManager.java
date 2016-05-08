/*
 * BulletManager.java
 *
 * Created: 4/20/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.ArrayList;

public class BulletManager {

    private GameScreen game;
    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    public BulletManager(GameScreen game) {
        this.game = game;
    }

    public Bullet fireBullet(float x, float y, float r, boolean isMine, String owner, String p) {
        for (Bullet b: bullets) {
            if (b.free) {
                b.free = false;
                b.isMine = isMine;
                b.applyVelocity(x, y, r);
                b.owner = owner;
                b.player = p;

                return b;
            }
        }
        Gdx.app.log("BulletManager", "Size is now " + bullets.size());
        Bullet b = new Bullet(game);
        b.isMine = isMine;
        System.out.println(x + " " + y);
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
