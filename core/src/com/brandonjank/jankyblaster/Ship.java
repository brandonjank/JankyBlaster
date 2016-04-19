package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Phelps on 4/17/2016.
 */
public class Ship {

    Texture texture;
    Sprite sprite;
    int rotation = 0;
    int moving = 0;
    World world;
    Body body;
    GameScreen game;
    boolean isLocal = false;


    public Ship(GameScreen game) {

        this.game = game;
        this.texture = game.shipTexture;
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
        shape.setRadius(5f);

        // Define the physics of the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f; // 50% rebound velocity
        fixtureDef.friction = 0.1f; // how easy or difficult it is for two solid objects to slide past one another

        // Apply the physics to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // We don't need the shape anymore
        shape.dispose();
    }

    public void handleKeyInput(InputEvent.Type event, int keycode, boolean remote) {
        if (remote) {
            System.out.println("Remote: " + event.toString());
        }

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
        Bullet b = new Bullet(game, this);
        game.bullets.add(b);
    }

    public void draw(Batch batch) {
        if (isLocal) {
            sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        }
        sprite.draw(batch);
    }

    public void updateRemote(Float x, Float y, Double r) {
        sprite.setPosition(x - texture.getWidth() / 2, y - texture.getHeight() / 2);
        sprite.setRotation( (float) Math.toDegrees(r) - 90 );
    }

    public void update(float delta) {

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


        float x, y;

        if (moving > 0) {
            x = 5000f * (float) Math.sin(-body.getAngle());
            y = 5000f * (float) Math.cos(-body.getAngle());
            body.applyLinearImpulse( x,  y, body.getPosition().x, body.getPosition().y, true);
        }
        else if (moving < 0) {
            x = -5000f * (float) Math.sin(-body.getAngle());
            y = -5000f * (float) Math.cos(-body.getAngle());
            body.applyLinearImpulse( x,  y, body.getPosition().x, body.getPosition().y, true);
        }

        if (rotation > 0) {
            body.setAngularVelocity(2f); // 1 revolution per second
        }
        else if (rotation < 0) {
            body.setAngularVelocity(-2f);
        }
        else {
            // stops rotation instantly
            body.setAngularVelocity(0f);
        }

        JSONObject data = new JSONObject();
        try {
            data.put("t", "s");
            data.put("p", game.username);
            data.put("x", body.getPosition().x);
            data.put("y", body.getPosition().y);

            float xr = 1 * (float) Math.cos(-body.getAngle());
            float yr = 1 * (float) Math.sin(-body.getAngle());
            double rr = Math.atan2(xr, yr);
            data.put("r", rr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        game.warp.sendUpdatePeers(data.toString().getBytes());
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
    }

}
