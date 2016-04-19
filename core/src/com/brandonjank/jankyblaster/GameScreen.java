package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.LobbyRequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Phelps on 4/17/2016.
 */
public class GameScreen implements Screen {

    private final JankyBlaster game;
    private Texture background;
    private Stage ui;
    public World world;
    private Box2DDebugRenderer debugRenderer;
    public Ship player;
    private Array<Body> bodies = new Array<Body>();
    public Vector3 unprojectVector = new Vector3();
    public OrthographicCamera camera;
    private int backgroundOffset;
    private int backgroundOffset2;
    public Texture shipTexture;
    public Texture bulletTexture;
    public WarpClient warp;
    public String username = "Guest" + (int )(Math.random() * 999 + 111);
    public HashMap<String, Ship> ships = new HashMap<String, Ship>();
    BitmapFont font = new BitmapFont();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Bullet> playerBullets = new ArrayList<Bullet>();

    public GameScreen(final JankyBlaster game) {

        WarpClient.initialize("4e3def12b0735bbcf1483ef90ede6f3eddd52b6fabb214cce114f83234e9d5f5","d3ff2f2008281e80e4fe34a7b24cf36bfaeb484985187bb37f15e910b9a2a99d");

        try {
            warp = WarpClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (warp != null) {
            warp.addConnectionRequestListener(new GameConnectionListener(warp));
            warp.addNotificationListener(new GameNotificationListener(this));
            warp.addLobbyRequestListener(new LobbyRequestListener() {
                @Override
                public void onJoinLobbyDone(LobbyEvent lobbyEvent) {
                    warp.getLiveLobbyInfo();
                }

                @Override
                public void onLeaveLobbyDone(LobbyEvent lobbyEvent) {

                }

                @Override
                public void onSubscribeLobbyDone(LobbyEvent lobbyEvent) {

                }

                @Override
                public void onUnSubscribeLobbyDone(LobbyEvent lobbyEvent) {

                }

                @Override
                public void onGetLiveLobbyInfoDone(LiveRoomInfoEvent liveRoomInfoEvent) {
                    String[] joinedUsers = liveRoomInfoEvent.getJoinedUsers();
                    for (String user: joinedUsers) {
                        System.out.println(user + " is in the Lobby!");
                        newPlayer(user);
                    }
                }
            });
            warp.connectWithUserName(username);
        }


        // injects the base Game object into the Screen scene if we ever need to access things on the outside
        this.game = game;

        // create the stage for the UI
        ui = new Stage();

        // create the physics world
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                /*
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                Object userDataA = a.getBody().getUserData();
                Object userDataB = b.getBody().getUserData();
                if (userDataA instanceof Ship && userDataB instanceof Bullet) {
                    world.destroyBody(((Bullet) userDataB).ship.body);
                }
                else if (userDataB instanceof Ship && userDataA instanceof Bullet) {
                    world.destroyBody(((Bullet) userDataA).ship.body);
                }
                */

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        int size = 350;
        wallAt(0, size, size, 10);
        wallAt(size, 0, 10, size);

        wallAt(0, -size, size, 10);
        wallAt(-size, 0, 10, size);


        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Table fills the screen, everything goes in the table
        VisTable table = new VisTable();
        table.setFillParent(true);
        ui.addActor(table);

        //Label test = new Label("Test", VisUI.getSkin());
        //table.add(test);

        shipTexture = new Texture(Gdx.files.internal("data/ship/ship.png"));
        bulletTexture = new Texture(Gdx.files.internal("data/ship/playerbullet.png"));

        // create a ship for the player and set the keyboard focus to it
        player = new Ship(this);
        player.launch(2000, 2000);

        // pass input to both of them
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GameInputProcessor(this));
        multiplexer.addProcessor(ui);
        Gdx.input.setInputProcessor(multiplexer);

        background = new Texture("data/background.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

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

    public void newPlayer(String player) {
        Ship newPlayer = new Ship(this);
        ships.put(player, newPlayer);
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


        for (Body body : bodies) {
            Object userData = body.getUserData();
            if (userData instanceof Ship) {
                ((Ship) userData).update(delta);
                ((Ship) userData).draw(game.batch);
            }
        }

        Iterator it = ships.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Ship ship = (Ship) pair.getValue();
            ship.draw(game.batch);
            font.draw(game.batch, pair.getKey().toString(), ship.sprite.getX()+16, ship.sprite.getY());
            font.draw(game.batch, "("+ ship.sprite.getX() +","+ ship.sprite.getY() +")", ship.sprite.getX()+16, ship.sprite.getY()-15);
            //it.remove();
        }
        // lot of code duplication :(
        // YOUR hit by PLAYER bullets here / remove here
        for (Bullet bullet : playerBullets) {
            bullet.update();
            bullet.draw(game.batch);
        }
        // remove YOUR bullets here, "fake" player HITS here
        for (Bullet bullet : bullets) {
            bullet.update();
            bullet.draw(game.batch);
            //font.draw(game.batch, "("+ bullet.sprite.getX() +","+ bullet.sprite.getX() +")", bullet.sprite.getX()+8, bullet.sprite.getY());

            Vector2 position = bullet.position;

            // remove bullets that exit the world
            int worldHeight = 2000;
            int worldWidth = 2000;
            if (bullet.sprite.getX() > worldWidth || bullet.sprite.getY() > worldHeight || bullet.sprite.getX() < 0 || bullet.sprite.getY() < 0) {
                //bullets.remove(bullet);
            }


            Iterator itt = ships.entrySet().iterator();
            while (itt.hasNext()) {
                Map.Entry pair = (Map.Entry)itt.next();
                Ship ship = (Ship) pair.getValue();

                // check if bullet hit a ship
                double bulletDistance = distance(ship.sprite.getX()+ship.sprite.getWidth()/2, ship.sprite.getY()+ship.sprite.getHeight()/2, bullet.sprite.getX(), bullet.sprite.getY());
                if (bulletDistance < 5) {
                    // bullet hit a ship
                    System.out.println("Your bullet hit "+pair.getKey().toString()+"!!");
                }

                //itt.remove();
            }

        }

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
}
