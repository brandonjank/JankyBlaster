package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


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
    Viewport viewport;
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
    ArrayList<String> chatLog = new ArrayList<String>();
    OrthogonalTiledMapRenderer mapRenderer;

    String serverUrl = "http://162.243.142.208:8080";

    public GameScreen(JankyBlaster game, String uname) {

        chatLog.add("Welcome to JankyBlaster, have fun and play nice!");
        chatLog.add("Version v0.2.1-alpha");

        if (uname != null) {
            username = uname;
        }
        else {
            username = "Guest" + (int )(Math.random() * 999 + 111);
        }

        // injects the base Game object into the Screen scene if we ever need to access things on the outside
        this.game = game;

        shapeRenderer = new ShapeRenderer();

        // create the stage for the UI
        ui = new Stage();

        // create the physics world
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(640, 480, camera);

        // create blue pixel for energy bar
        energyBarTexture = new Texture(createEnergyBarPixmap(1,1,0,0,1,0.8f));

        // create a ship for the player and set the keyboard focus to it
        player = new Ship(this, socketID, username);
        player.launch(950, 1100);

        // pass input to both of them
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GameInputProcessor(this));
        multiplexer.addProcessor(ui);
        Gdx.input.setInputProcessor(multiplexer);

        background = new Texture("data/background.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        connectSocket();
        configSocketEvents();

        TiledMap map = new TmxMapLoader().load("data/maps/test.tmx");
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int columns = layer.getWidth();
        int rows = layer.getHeight();

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);

                if (cell != null) {
                    int size = 16;
                    int x = (i * size) + 8;
                    int y = (j * size) + 8;
                    wallAt(x, y, 8, 8);
                }
            }
        }

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1);


    }

    public void connectSocket(){
        try {
            socket = IO.socket(serverUrl);
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }

        // TODO: handle when not connected
    }

    public void configSocketEvents(){

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
                chatLog.add("You have connected to: " + serverUrl);
                socket.emit("name", username);
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    socketID = data.getString("id");

                    Gdx.app.log("SocketIO", "My socket ID: " + socketID);
                    chatLog.add("Your socket ID is: " + socketID);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "ERROR: Could not get socket ID from server.");
                    chatLog.add("ERROR: Could not get socket ID from server.");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    newPlayer(data.getString("id"), data.getString("name"));
                    Gdx.app.log("SocketIO", "New Player: " + data.getString("id"));
                    chatLog.add(data.getString("id") + " has joined the game.");
                } catch(JSONException e) {
                    e.printStackTrace();
                    Gdx.app.log("SocketIO", "ERROR: Could not get new player ID from server.");
                    chatLog.add("ERROR: Could not get new player ID from server.");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "Disconnecting Player: " + id);
                    chatLog.add(id + " has disconnected from the game.");
                    ships.remove(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        updatePlayer(id, x, y, r, 1.0f);
                    }
                } catch(JSONException e){
                    e.printStackTrace();
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
                    float e = ((Double) data.getDouble("e")).floatValue();
                    updatePlayer(id, x, y, r, e);
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
                    String p = data.getString("p");
                    float x = ((Double) data.getDouble("x")).floatValue();
                    float y = ((Double) data.getDouble("y")).floatValue();
                    float r = ((Double) data.getDouble("r")).floatValue();
                    spawnBullet(x, y, r, id, p);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void wallAt(int x, int y, int width, int height) {
        BodyDef wallsDef = new BodyDef();
        wallsDef.position.set(x, y);
        Body wallsBody = world.createBody(wallsDef);
        PolygonShape wallsShape = new PolygonShape();
        wallsShape.setAsBox(width, height);
        wallsBody.createFixture(wallsShape, 1f);
        wallsShape.dispose();
    }

    public void spawnBullet(float x, float y, float r, String owner, String p) {
        Assets.shootBulletSound.play();
        bulletManager.fireBullet(x, y, r, false, owner, p);
    }

    public void newPlayer(String socketID, String username) {
        Ship newPlayer = new Ship(this, socketID, username);
        System.out.println(newPlayer.socketID);
        ships.put(socketID, newPlayer);
    }

    public void updatePlayer(String player, Float x, Float y, Double r, Float e) {
        Ship playerShip = ships.get(player);
        if (playerShip != null) {
            playerShip.updateRemote(x, y, r, e);
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

        world.step(delta, 10, 10);

        Camera uiCamera = ui.getCamera();

        ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        camera.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);
        camera.update();
        //camera.zoom = 5;

        // update and render our bodies
        world.getBodies(bodies);

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0, 0, 0, background.getWidth() * 200, background.getHeight() * 200);

        game.batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render();

        game.batch.begin();

        font.draw(game.batch, "("+SCORE+")", player.sprite.getX()+16, player.sprite.getY()-10);

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
            //System.out.println(ship);
            ship.draw(game.batch);
            font.draw(game.batch, ship.username, ship.sprite.getX()+16, ship.sprite.getY());
            // draw enemy energy bar
            game.batch.draw(energyBarTexture, ship.sprite.getX()+16, ship.sprite.getY()+5, (ship.energy/ship.energyMax)*energyBarWidth, energyBarHeight);
        }

        bulletManager.update();

        shapeRenderer.setProjectionMatrix(camera.combined);

        for (final Bullet bullet : bulletManager.bullets) {
            // skip free bullets
            if (bullet.free) continue;

            // destroy bullets that hit walls
            world.QueryAABB(new QueryCallback() {
                @Override
                public boolean reportFixture(Fixture fixture) {
                    if (fixture != player.fixture) {
                        bulletManager.destroyBullet(bullet);
                    }
                    return false;
                }
            }, bullet.position.x, bullet.position.y, bullet.position.x+16, bullet.position.y+16);

            // check for player impacts
            if (bullet.isMine) {
                // check if the player's bullet hit another player
                Iterator itt = ships.entrySet().iterator();
                while (itt.hasNext()) {
                    Ship ship = (Ship)((Map.Entry)itt.next()).getValue();
                    if (checkCollision(ship, bullet)) {
                        ship.energy = ship.energy - ship.energyCostPerShot*1.5f;
                        if (ship.energy <= 0) {
                            // enemy destroyed
                            SCORE++;
                            Assets.explodeEnemySound.play();
                            chatLog.add("You destroyed " + ship.username + "!!");
                        } else {
                            // enemy survived
                            Assets.hitSound.play();
                            chatLog.add("You hit " + ship.username + "!!");
                        }
                        bulletManager.destroyBullet(bullet);
                    }
                }
            } else {
                // check if the player got hit by another player's bullet
                if (checkCollision(player, bullet)) {
                    player.energy = player.energy - player.energyCostPerShot*1.5f;
                    if (player.energy <= 0) {
                        // player "died"
                        SCORE--;
                        Assets.explodePlayerSound.play();
                        chatLog.add("You were destroyed by " + bullet.player + "!!");
                        // respawn
                        player.body.setLinearVelocity(0, 0);
                        player.body.setTransform(950, 1100, 0f);
                        player.energy = player.energyMax;
                    } else {
                        // player survived
                        Assets.hit2Sound.play();
                        chatLog.add("You got hit by " + bullet.player + "!");
                    }
                    bulletManager.destroyBullet(bullet);
                }
            }
        }

        // regen player ship energy
        if (player.energy < player.energyMax) {
            player.energy = Math.min(player.energy + player.energyGainedPerTick, player.energyMax);
        }
        // display player ship energy bar
        game.batch.draw(energyBarTexture, player.sprite.getX()+16, player.sprite.getY()+5, (player.energy/player.energyMax)*energyBarWidth, energyBarHeight);

        bulletManager.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(ui.getViewport().getCamera().combined);
        game.batch.begin();

        int cyo = 0;
        int size = chatLog.size();

        for (int i = size - 1; i >= 0 && i > size - 6; i--) {
            String msg = chatLog.get(i);
            font.draw(game.batch, msg, 10, (cyo * 16) + 16);
            cyo++;
        }

        game.batch.end();

        ui.draw();

        // debug physics, fun
        // debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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

    Pixmap createEnergyBarPixmap (int width, int height, int r, int g, int b, float a) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, a);
        pixmap.fill();
        return pixmap;
    }

    private Object[] appendValue(Object[] obj, Object newObj) {
        ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
        temp.add(newObj);
        return temp.toArray();
    }
}
