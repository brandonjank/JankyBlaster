package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phelps on 4/17/2016.
 */
public class Bullet {

    GameScreen game;
    Ship ship;
    Texture texture;
    Sprite sprite;
    float angle;
    Vector2 position = new Vector2();

    public Bullet(GameScreen game, float x, float y, double r) {
        this.game = game;
        texture = game.bulletTexture;
        sprite = new Sprite(texture);
        angle = (float) r;
        sprite.setSize(texture.getWidth(), texture.getHeight());
        sprite.setPosition(x, y);
    }

    public Bullet(GameScreen game, Ship ship) {

        this.game = game;
        this.ship = ship;
        texture = game.bulletTexture;

        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        sprite.setPosition(ship.body.getPosition().x, ship.body.getPosition().y);

        angle = ship.body.getAngle();


        float x = 1f * (float) Math.sin(-angle);
        float y = 1f * (float) Math.cos(-angle);
        JSONObject data = new JSONObject();
        try {
            data.put("t", "b");
            data.put("p", game.username);
            data.put("x", x);
            data.put("y", y);
            data.put("r", angle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        game.warp.sendUpdatePeers(data.toString().getBytes());

    }


    public void update() {
        float x = 1f * (float) Math.sin(-angle);
        float y = 1f * (float) Math.cos(-angle);
        double r = Math.atan2(x, y);
        float rr = (float) Math.toDegrees(r) - 180; // real rotation
        sprite.setPosition(sprite.getX() + x, sprite.getY() + y);
        sprite.setRotation( rr );
        position.set(x, y);
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }


}
