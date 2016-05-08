/*
 * Ship.java
 *
 * Created: 4/17/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Ship {

    Texture texture;
    Sprite sprite;
    int rotation = 0;
    int moving = 0;
    float energyMax = 1000;
    float energy = energyMax;
    float energyCostPerShot = 500;
    float energyGainedPerTick = 5;
    float energyCostPerBoost = 10;
    World world;
    Body body;
    GameScreen game;
    String socketID;
    String username;
    Fixture fixture;
    boolean boosting = false;
    boolean isLocal = false;


    public Ship(GameScreen game, String socketID, String username) {
        this.socketID = socketID;
        this.username = username;
        this.game = game;
        this.texture = Assets.shipTexture;
        this.world = game.world;
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth()/game.P2M, texture.getHeight()/game.P2M);
        sprite.setOriginCenter();
    }

    public void launch(int x, int y) {
        isLocal = true;
        // Create the body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // Create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        // Create the shape of the body
        CircleShape shape = new CircleShape();
        shape.setRadius(1);

        // Define the physics of the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0.8f; // 80% rebound velocity
        fixtureDef.friction = 0f; // how easy or difficult it is for two solid objects to slide past one another
        //fixtureDef.

        // Apply the physics to the body
        fixture = body.createFixture(fixtureDef);

        // We don't need the shape anymore
        shape.dispose();
    }

    public void handleKeyInput(InputEvent.Type event, int keycode) {
        if (event == InputEvent.Type.keyDown) {
            if (keycode == Input.Keys.W) { moving   =  1; }
            if (keycode == Input.Keys.S) { moving   = -1; }
            if (keycode == Input.Keys.A) { rotation =  1; }
            if (keycode == Input.Keys.D) { rotation = -1; }
            if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) { boosting = true; }
            if (keycode == Input.Keys.SPACE) { shoot(); }
        }
        if (event == InputEvent.Type.keyUp) {
            if (keycode == Input.Keys.W) { moving   = 0; }
            if (keycode == Input.Keys.S) { moving   = 0; }
            if (keycode == Input.Keys.A) { rotation = 0; }
            if (keycode == Input.Keys.D) { rotation = 0; }
            if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) { boosting = false; }
        }
    }

    public void shoot() {
        if (energy >= energyCostPerShot) {
            float x = 1f * (float) Math.sin(-body.getAngle()) + body.getPosition().x;
            float y = 1f * (float) Math.cos(-body.getAngle()) + body.getPosition().y;
            Assets.shootBullet2Sound.play();
            game.bulletManager.fireBullet(body.getPosition().x, body.getPosition().y, getRealAngle(), true, game.socketID, game.username);
            energy -= energyCostPerShot;

            JSONObject data = new JSONObject();
            try {
                data.put("s", game.socketID);
                data.put("x", x);
                data.put("y", y);
                data.put("r", getRealAngle());
                data.put("p", game.username);
                game.socket.emit("bullet", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Batch batch) {
        if (isLocal) {
            sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        }
        sprite.draw(batch);
    }

    public void updateRemote(Float x, Float y, Double r, Float e) {
        sprite.setPosition(x - (texture.getWidth() / 2f)/game.P2M, y - (texture.getHeight() / 2f) / game.P2M);
        sprite.setRotation( (float) Math.toDegrees(r) - 90f );
        energy = e;
    }

    private float getRealAngle() {
        float xr = 1f * (float) Math.cos(-body.getAngle());
        float yr = 1f * (float) Math.sin(-body.getAngle());
        return (float) Math.atan2(xr, yr);
    }

    float dTimer = 0;

    public void update(float delta) {

        dTimer += delta;

        // calc velocity


        float x = 0f;
        float y = 0f;

        float speed = (float) Math.sqrt( Math.pow((double) body.getLinearVelocity().x, 2d) + Math.pow((double) body.getLinearVelocity().y, 2d) );

        if (moving > 0) {
            x = 10f * (float) Math.sin(-body.getAngle());
            y = 10f * (float) Math.cos(-body.getAngle());
            //body.applyLinearImpulse( x,  y, body.getPosition().x, body.getPosition().y, true);
        }
        else if (moving < 0) {
            x = -5f * (float) Math.sin(-body.getAngle());
            y = -5f * (float) Math.cos(-body.getAngle());
            //body.applyLinearImpulse( x,  y, body.getPosition().x, body.getPosition().y, true);
        }

        body.applyForceToCenter(x, y, true);

        if (boosting && game.player.energy > game.player.energyCostPerBoost) {
            game.player.energy -= game.player.energyCostPerBoost;
            body.setLinearVelocity(Math.max(-8, Math.min(8, body.getLinearVelocity().x)), Math.max(-8, Math.min(8, body.getLinearVelocity().y)));
        }
        else {
            body.setLinearVelocity(Math.max(-3, Math.min(3, body.getLinearVelocity().x)), Math.max(-3, Math.min(3, body.getLinearVelocity().y)));
        }


        if (rotation > 0) {
            body.setAngularVelocity(3f);
        }
        else if (rotation < 0) {
            body.setAngularVelocity(-3f);
        }
        else {
            body.setAngularVelocity(0f);
        }

        // movement thread
        if (dTimer > 0.05) {
            dTimer = 0;
            new MoveThread(game.socket, game.socketID, body.getPosition().x, body.getPosition().y, getRealAngle(), energy).start();
        }

        sprite.setPosition(body.getPosition().x - (texture.getWidth() / game.P2M) / 2 , body.getPosition().y - (texture.getWidth() / game.P2M) / 2);

    }

}
