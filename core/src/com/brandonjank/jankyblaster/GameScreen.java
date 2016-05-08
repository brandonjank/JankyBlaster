/*
 * GameScreen.java
 *
 * Created: 4/17/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.List;


public class GameScreen implements Screen {

    int P2M = 16;
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
    String username;
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
    HashMap<String, Integer> score = new HashMap<String, Integer>();
    OrthogonalTiledMapRenderer mapRenderer;
    Map<String, Integer> scoreSorted;
    TextureRegion tr;

    String serverUrl = "http://162.243.142.208:8080";

    public GameScreen(JankyBlaster game) {
        username = game.prefs.getString("username", "Guest" + (int )(Math.random() * 999 + 111));

        chatLog.add("Welcome to JankyBlaster, have fun and play nice!");
        chatLog.add("Version: v0.2.1-alpha");
        chatLog.add("Your username is: "+username);

        // injects the base Game object into the Screen scene if we ever need to access things on the outside
        this.game = game;

        shapeRenderer = new ShapeRenderer();

        // create the stage for the UI
        ui = new Stage();

        // create the physics world
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera(Gdx.graphics.getWidth() / P2M, Gdx.graphics.getHeight() / P2M);
        //viewport = new FitViewport(pixelsToMeteres(640), pixelsToMeteres(480), camera);

        // create blue pixel for energy bar
        energyBarTexture = new Texture(createEnergyBarPixmap(1,1,0,0,1,0.8f));

        //player.launch(1, 1);

        // pass input to both of them
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GameInputProcessor(this));
        multiplexer.addProcessor(ui);
        Gdx.input.setInputProcessor(multiplexer);

        background = Assets.backgroundTexture;
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        tr = new TextureRegion(background);

        connectSocket();
        configSocketEvents();

        TiledMap map = new TmxMapLoader().load("data/maps/test2.tmx");
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int columns = layer.getWidth();
        int rows = layer.getHeight();

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);

                if (cell != null) {
                    int size = 1;
                    int x = (i * size);
                    int y = (j * size);
                    wallAt(x, y, 0.5f, 0.5f);
                }
            }
        }

        // create a ship for the player and set the keyboard focus to it
        player = new Ship(this, socketID, username);
        player.launch(columns/2, rows/2);

        mapRenderer = new OrthogonalTiledMapRenderer(map,  1f / P2M);


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
        }).on("chat", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String p = data.getString("p");
                    String m = data.getString("m");
                    chatLog.add("<" + p + "> " + m);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("death", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String p = data.getString("p");
                    if (score.get(p) != null) {
                        Integer s = score.get(p);
                        score.put(p, s + 1 );
                    }
                    else {
                        score.put(p, 1 );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void wallAt(int x, int y, float width, float height) {
        BodyDef wallsDef = new BodyDef();
        wallsDef.position.set(x + 0.5f, y + 0.5f);
        Body wallsBody = world.createBody(wallsDef);
        PolygonShape wallsShape = new PolygonShape();
        wallsShape.setAsBox(width, height);
        wallsBody.createFixture(wallsShape, 1f);
        wallsShape.dispose();
    }

    public void spawnBullet(float x, float y, float r, String owner, String p) {
        // scale audio by distance from player, 2 screen widths (20 units) or more should be silent.
        float distanceScale =  Math.max(0.0f, Math.min(1.0f, 20/(float)distance(player.sprite.getX(), player.sprite.getY(), x, y)));
        Assets.shootBulletSound.play(game.prefs.getFloat("volume",1f)*game.prefs.getFloat("volumeEffects",1f)*distanceScale);
        bulletManager.fireBullet(x, y, r, false, owner, p);
    }

    public void newPlayer(String socketID, String username) {
        Ship newPlayer = new Ship(this, socketID, username);
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
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public boolean checkCollision(Ship ship, Bullet bullet) {
        double bulletDistance = distance(ship.sprite.getX()+((ship.sprite.getWidth()/2)/P2M), ship.sprite.getY()+((ship.sprite.getHeight()/2)/P2M), bullet.sprite.getX(), bullet.sprite.getY());
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
        //camera.zoom = 1;
        world.getBodies(bodies);

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0f, 0f, 200f, 200f);

        game.batch.end();

        //mapRenderer.setView(camera);

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

        bulletManager.update();

        // update enemy ships
        Iterator it = ships.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Ship ship = (Ship) pair.getValue();
            //System.out.println(ship);
            ship.draw(game.batch);
            font.draw(game.batch, ship.username, ship.sprite.getX()+16, ship.sprite.getY());
            // draw enemy energy bar
            game.batch.draw(energyBarTexture, ship.sprite.getX()+1, ship.sprite.getY()+(5/P2M), ((ship.energy/ship.energyMax)*energyBarWidth) / P2M, energyBarHeight / P2M);
        }

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
            }, bullet.position.x, bullet.position.y, bullet.position.x+(16f/P2M), bullet.position.y+(16f/P2M));

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
                            Assets.explodeEnemySound.play(game.prefs.getFloat("volume",1f)*game.prefs.getFloat("volumeEffects",1f));
                            chatLog.add("You destroyed " + ship.username + "!!");

                            JSONObject data = new JSONObject();
                            try {
                                data.put("p", username);
                                socket.emit("death", data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            // enemy survived
                            Assets.hitSound.play(game.prefs.getFloat("volume",1f)*game.prefs.getFloat("volumeEffects",1f));
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
                        Assets.explodePlayerSound.play(game.prefs.getFloat("volume",1f)*game.prefs.getFloat("volumeEffects",1f));
                        chatLog.add("You were destroyed by " + bullet.player + "!!");

                        JSONObject data = new JSONObject();
                        try {
                            data.put("p", bullet.player);
                            socket.emit("death", data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // respawn
                        player.body.setLinearVelocity(0, 0);
                        player.body.setTransform(950, 1100, 0f);
                        player.energy = player.energyMax;


                    } else {
                        // player survived
                        Assets.hit2Sound.play(game.prefs.getFloat("volume",1f)*game.prefs.getFloat("volumeEffects",1f));
                        chatLog.add(player.username + " got hit by " + bullet.player + "!");
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
        game.batch.draw(energyBarTexture, player.sprite.getX()+1, player.sprite.getY()+(5/P2M), ((player.energy/player.energyMax)*energyBarWidth) / P2M, energyBarHeight / P2M);

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

        if (score != null) {
            int syo = 0;
            Iterator sit = score.entrySet().iterator();
            while (sit.hasNext()) {
                Map.Entry pair = (Map.Entry)sit.next();
                font.draw(game.batch, pair.getKey() + ": " + pair.getValue(), Gdx.graphics.getWidth() - 100,  Gdx.graphics.getHeight() - (syo * -16) - 32);
                syo++;
            }
        }

        game.batch.end();

        ui.draw();

        // debug physics, fun
        debugRenderer.render(world, camera.combined);


    }

    @Override
    public void resize(int width, int height) {
        //viewport.update(width, height);
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

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
