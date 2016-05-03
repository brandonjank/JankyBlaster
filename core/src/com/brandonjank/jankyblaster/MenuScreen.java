/*
 * MenuScreen.java
 *
 * Created: 4/17/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public class MenuScreen implements Screen {

    final JankyBlaster game;
    private Stage stage;
    private OrthographicCamera camera;
    String username;

    private ParticleEffect engineParticle;

    public MenuScreen (final JankyBlaster game) {
        this.game = game;

        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Table fills the screen, everything goes in the table
        VisTable table = new VisTable();
        table.setFillParent(true);
        stage.addActor(table);

        //Label nameLabel = new Label("Name:", VisUI.getSkin());
        TextField textfield = new TextField("", VisUI.getSkin());
        textfield.setMessageText("Pilot Name");
        textfield.setAlignment(Align.center);
        //table.add(nameLabel);
        table.add(textfield);
        table.row();

        textfield.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                username = textField.getText();
            }
        });

        // Draw a button to start the game
        final TextButton startGameButton = new TextButton("Start Game", VisUI.getSkin());
        table.add(startGameButton);
        table.row();

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, username));
                dispose();
            }
        });

        // Draw a button to open options screen
        final TextButton optionsButton = new TextButton("Options", VisUI.getSkin());
        table.add(optionsButton);
        table.row();

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.setScreen(new GameScreen(options));
                //dispose();
            }
        });

        // Draw a button to exit the game
        final TextButton exitGameButton = new TextButton("Exit", VisUI.getSkin());
        table.add(exitGameButton);
        table.row();

        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        engineParticle = new ParticleEffect();
        engineParticle.load(Gdx.files.internal("data/particles/engine.p"), Gdx.files.internal(""));
        engineParticle.getEmitters().first().setPosition(Gdx.input.getX(), 480-Gdx.input.getY());
        engineParticle.start();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(Assets.menuBackgroundTexture, 0, 0);
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        game.batch.begin();
        engineParticle.update(delta);
        engineParticle.getEmitters().first().durationTimer = 0;
        engineParticle.setPosition(Gdx.input.getX(), 480-Gdx.input.getY());
        engineParticle.draw(game.batch);
        game.batch.end();
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
