package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameScreen implements Screen {

    private JankyBlaster game;
    public Socket socket;
    private Texture background;
    private Stage ui;
    public World world;
    private Box2DDebugRenderer debugRenderer;
    public Ship player;
    private Array<Body> bodies = new Array<Body>();
    public OrthographicCamera camera;
    public Texture shipTexture;
    public Texture bulletTexture;
    public String username;
    public HashMap<String, Ship> ships = new HashMap<String, Ship>();
    BitmapFont font = new BitmapFont();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Bullet> playerBullets = new ArrayList<Bullet>();
    int worldHeightOffset = 2000;
    int worldWidthOffset = 2000;
    int worldSize = 400;
    static int SCORE = 0;
    String socketID;
    BulletManager bulletManager = new BulletManager(this);
    private ShapeRenderer shapeRenderer;
    Texture energyBarTexture;
    float energyBarWidth = 50f;
    float energyBarHeight = 5f;

    public GameScreen(JankyBlaster game, String uname) {

        if (uname != null) {
            username = "Guest" + (int )(Math.random() * 999 + 111);
        }
        else {
            username = uname;
        }

        // injects the base Game object into the Screen scene if we ever need to access things on the outside
        this.game = game;

        shapeRenderer = new ShapeRenderer();

        // create the stage for the UI
        ui = new Stage();

        // create the physics world
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        // create debug arena walls
        wallAt(0, worldSize, worldSize, 10);
        wallAt(worldSize, 0, 10, worldSize);

        wallAt(0, -worldSize, worldSize, 10);
        wallAt(-worldSize, 0, 10, worldSize);


        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Table fills the screen, everything goes in the table
        VisTable table = new VisTable();
        table.setFillParent(true);
        ui.addActor(table);

        //Label test = new Label("Test", VisUI.getSkin());
        //table.add(test);

        shipTexture = new Texture(Gdx.files.internal("data/ship/ship.png"));
        bulletTexture = new Texture(Gdx.files.internal("data/ship/playerbullet.png"));

        // create blue pixel for energy bar
        energyBarTexture = new Texture(createEnergyBarPixmap(1,1,0,0,1));

        // create a ship for the player and set the keyboard focus to it
        player = new Ship(this, socketID, username);
        player.launch(2000, 2000);

        // pass input to both of them
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GameInputProcessor(this));
        multiplexer.addProcessor(ui);
        Gdx.input.setInputProcessor(multiplexer);

        background = new Texture("data/background.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        connectSocket();
        configSocketEvents();

    }

    public void connectSocket(){
        try {
            socket = IO.socket("http://45.32.6.119:8080");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void configSocketEvents(){

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
                socket.emit("name", username);
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    socketID = data.getString("id");

                    Gdx.app.log("SocketIO", "My ID: " + socketID);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    newPlayer(data.getString("id"), data.getString("name"));
                    Gdx.app.log("SocketIO", "New Player Connect: " + data.getString("id"));
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                    /*try {
                        id = data.getString("id");
                        friendlyPlayers.remove(id);
                    }catch(JSONException e){
                        Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                    }*/
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for(int i = 0; i < objects.length(); i++){
                        String id = objects.getJSONObject(i).getString("id");
                        newPlayer(id, objects.getJSONObject(i).getString("name"));
                        float x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                        float y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                        double r = (Double) objects.getJSONObject(i).getDouble("r");
                        updatePlayer(id, x, y, r);
                    }
                } catch(JSONException e){

                }
            }
        }).on("position", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("s");
                    float x = ((Double) data.getDouble("x")).floatValue();
                    float y = ((Double) data.getDouble("y")).floatValue();
                    double r = data.getDouble("r");
                    updatePlayer(id, x, y, r);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("bullet", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("s");
                    float x = ((Double) data.getDouble("x")).floatValue();
                    float y = ((Double) data.getDouble("y")).floatValue();
                    float r = ((Double) data.getDouble("r")).floatValue();
                    spawnBullet(x, y, r);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void wallAt(int x, int y, int width, int height) {
        BodyDef wallsDef = new BodyDef();
        wallsDef.position.set(2000 + x, 2000 + y);
        Body wallsBody = world.createBody(wallsDef);
        PolygonShape wallsShape = new PolygonShape();
        wallsShape.setAsBox(width, height);
        wallsBody.createFixture(wallsShape, 1f);
        wallsShape.dispose();
    }

    public void spawnBullet(float x, float y, float r) {
        bulletManager.fireBullet(x, y, r, false);
    }

    public void newPlayer(String socketID, String username) {
        Ship newPlayer = new Ship(this, socketID, username);
        ships.put(socketID, newPlayer);
    }

    public void updatePlayer(String player, Float x, Float y, Double r) {
        Ship playerShip = ships.get(player);
        if (playerShip != null) {
            playerShip.updateRemote(x, y, r);
        }
    }

    @Override
    public void show() {

    }

    public double distance(float x1, float y1, float x2, float y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public boolean checkCollision(Ship ship, Bullet bullet) {
        double bulletDistance = distance(ship.sprite.getX()+ship.sprite.getWidth()/2, ship.sprite.getY()+ship.sprite.getHeight()/2, bullet.sprite.getX(), bullet.sprite.getY());
        if (bulletDistance < (ship.sprite.getWidth()/2) - 2) {
            return true;
        }
        return false;
    }

    @Override
    public void render(float delta) {
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Camera uiCamera = ui.getCamera();

        // update the UI
        ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        camera.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);
        camera.update();

        world.step(delta, 8, 3);

        // update and render our bodies
        world.getBodies(bodies);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(background, 0, 0, 0, 0, background.getWidth() * 200, background.getHeight() * 200);

        font.draw(game.batch, "("+SCORE+")", player.sprite.getX()+16, player.sprite.getY()-10);


        if (player.energy < player.energyMax) {
            player.energy = Math.min(player.energy + player.energyGainedPerTick, player.energyMax);
        }
        game.batch.draw(energyBarTexture, player.sprite.getX()+16, player.sprite.getY()+5, (player.energy/player.energyMax)*energyBarWidth, energyBarHeight);

        for (Body body : bodies) {
            Object userData = body.getUserData();
            if (userData instanceof Ship) {
                ((Ship) userData).update(delta);
                ((Ship) userData).draw(game.batch);
            }
        }


        // update enemy ships
        Iterator it = ships.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Ship ship = (Ship) pair.getValue();
            ship.draw(game.batch);
            font.draw(game.batch, pair.getKey().toString(), ship.sprite.getX()+16, ship.sprite.getY());
            //it.remove();
        }

        bulletManager.update();

        shapeRenderer.setProjectionMatrix(camera.combined);



        for (Bullet bullet : bulletManager.bullets) {
            if (bullet.free) continue; // fast break on "dead" bullets
            if (bullet.position.x > worldWidthOffset + worldSize || bullet.position.y > worldHeightOffset + worldSize || bullet.position.y < worldWidthOffset - worldSize || bullet.position.y < worldHeightOffset - worldSize) {
                bulletManager.destroyBullet(bullet);
            } else {
                if (bullet.isMine) {
                    // check if the player's bullet hit another player
                    Iterator itt = ships.entrySet().iterator();
                    while (itt.hasNext()) {
                        Ship ship = (Ship)((Map.Entry)itt.next()).getValue();
                        if (checkCollision(ship, bullet)) {
                            System.out.println("You hit " + ship.username + "!!");
                            SCORE++;
                            bulletManager.destroyBullet(bullet);
                        }
                    }
                }
                else {
                    // check if the player got hit by another player's bullet
                    if (checkCollision(player, bullet)) {
                        System.out.println("You got hit!"); // TODO: hit by?
                        SCORE--;
                        bulletManager.destroyBullet(bullet);
                        player.body.setTransform(2000f, 2000f, 0f);
                        player.body.setLinearVelocity(0, 0);
                    }
                }
            }
        }

        bulletManager.draw(game.batch);


        game.batch.end();

        ui.draw();

        // debug physics, fun
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    Pixmap createEnergyBarPixmap (int width, int height, int r, int g, int b) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, 1);
        pixmap.fill();
        return pixmap;
    }
}
