/*
 * MenuScreen.java
 *
 * Created: 4/17/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class MenuScreen implements Screen {

    final JankyBlaster game;
    Vector3 cursor;
    private Stage stage;

    public MenuScreen(final JankyBlaster game) {
        this.game = game;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        cursor = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);

        this.stage = new Stage(new FitViewport(640, 480));
        Gdx.input.setInputProcessor(stage);

        // Table fills the screen, everything goes in the table
        VisTable table = new VisTable();
        table.setFillParent(true);
        stage.addActor(table);

        // start button
        final VisTextButton startGameButton = new VisTextButton("PLAY");
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        table.add(startGameButton);
        table.row().pad(3.0f);

        // controls button
        final VisTextButton controlsButton = new VisTextButton("CONTROLS");
        controlsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ControlsScreen(game));
                dispose();
            }
        });
        table.add(controlsButton);
        table.row().pad(3.0f);

        // options button
        final VisTextButton optionsButton = new VisTextButton("OPTIONS");
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionsScreen(game));
                dispose();
            }
        });
        table.add(optionsButton);
        table.row().pad(3.0f);

        // exit button
        final VisTextButton exitGameButton = new VisTextButton("EXIT GAME");
        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        table.add(exitGameButton);
        table.row().pad(3.0f);

        // cursor effect
        Assets.engineParticle.getEmitters().first().setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        Assets.engineParticle.start();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cursor.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        cursor = stage.getViewport().unproject(cursor);

        game.batch.begin();
        game.batch.draw(Assets.menuBackgroundTexture, 0, 0);
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // draw cursor effect
        Assets.engineParticle.update(delta);
        Assets.engineParticle.getEmitters().first().durationTimer = 0;
        Assets.engineParticle.setPosition(cursor.x, cursor.y);
        game.batch.begin();
        Assets.engineParticle.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
