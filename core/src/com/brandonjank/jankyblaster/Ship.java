package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jank6275 on 4/17/2016.
 */
public class Ship {

    Texture texture;
    Sprite sprite;
    int rotation = 0;
    int moving = 0;
    float energyMax = 1000;
    float energy = energyMax;
    float energyCostPerShot = 500;
    float energyGainedPerTick = 5;
    World world;
    Body body;
    GameScreen game;
    String socketID;
    String username;
    Fixture fixture;
    boolean isLocal = false;


    public Ship(GameScreen game, String socketID, String username) {
        this.socketID = socketID;
        this.username = username;
        this.game = game;
        this.texture = Assets.shipTexture;
        this.world = game.world;
        sprite = new Sprite(texture);
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
        shape.setRadius(8f);

        // Define the physics of the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.restitution = 0.8f; // 80% rebound velocity
        fixtureDef.friction = 0.5f; // how easy or difficult it is for two solid objects to slide past one another

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
            if (keycode == Input.Keys.SPACE) { shoot(); }
        }
        if (event == InputEvent.Type.keyUp) {
            if (keycode == Input.Keys.W) { moving   = 0; }
            if (keycode == Input.Keys.S) { moving   = 0; }
            if (keycode == Input.Keys.A) { rotation = 0; }
            if (keycode == Input.Keys.D) { rotation = 0; }
        }
    }

    public void shoot() {
        if (energy >= energyCostPerShot) {
            float x = 1f * (float) Math.sin(-body.getAngle()) + body.getPosition().x;
            float y = 1f * (float) Math.cos(-body.getAngle()) + body.getPosition().y;
            Assets.shootBullet2Sound.play();
            game.bulletManager.fireBullet(x, y, getRealAngle(), true, game.socketID, game.username);
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
        sprite.setPosition(x - texture.getWidth() / 2, y - texture.getHeight() / 2);
        sprite.setRotation( (float) Math.toDegrees(r) - 90 );
        energy = e;
    }

    private float getRealAngle() {
        float xr = 1 * (float) Math.cos(-body.getAngle());
        float yr = 1 * (float) Math.sin(-body.getAngle());
        return (float) Math.atan2(xr, yr);
    }

    float dTimer = 0;

    public void update(float delta) {

        dTimer += delta;

        /*
        if (currentSpeed < maxspeed)
        {
            turn off dampening
            apply thrust force
        }
        else
        {
            turn on dampening (so player decelerates until reaching max speed)
            deltaVelocity = timeStep * force / mass
            if ((velocity + deltaVelocity).length() < currentSpeed)
                slowing down so apply the force
            else
                trying to speed up when already moving too fast, no thrust for you
        }
        */

        // calc velocity


        float x, y;


        if (moving > 0) {
            x = 100f * (float) Math.sin(-body.getAngle());
            y = 100f * (float) Math.cos(-body.getAngle());
            body.applyLinearImpulse( x,  y, body.getPosition().x, body.getPosition().y, true);
        }
        else if (moving < 0) {
            x = -50f * (float) Math.sin(-body.getAngle());
            y = -50f * (float) Math.cos(-body.getAngle());
            body.applyLinearImpulse( x,  y, body.getPosition().x, body.getPosition().y, true);
        }

        if (rotation > 0) {
            body.setAngularVelocity(2f); // 1 revolution per second
        }
        else if (rotation < 0) {
            body.setAngularVelocity(-2f);
        }
        else {
            body.setAngularVelocity(0f);
        }

        // movement thread
        if (dTimer > 0.05) {
            dTimer = 0;
            new MoveThread(game.socket, game.socketID, body.getPosition().x, body.getPosition().y, getRealAngle(), energy).start();
        }

        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);

    }

}
