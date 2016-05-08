/*
 * ControlsScreen.java
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ControlsScreen implements Screen {

    final JankyBlaster game;
    Vector3 cursor;
    private Stage stage;

    public ControlsScreen(final JankyBlaster game) {
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

        // create table filling the screen
        VisTable table = new VisTable();
        table.setFillParent(true);
        table.align(Align.center | Align.bottom);

        // controls image
        final VisImage controlsImage = new VisImage(Assets.controlsTexture);
        table.add(controlsImage).pad(50f);
        table.row();

        // back button
        final VisTextButton backButton = new VisTextButton("GO BACK");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        table.add(backButton);
        table.row();

        // add table to the stage
        stage.addActor(table);

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
