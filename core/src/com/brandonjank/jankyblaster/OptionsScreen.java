/*
 * Options Screen.java
 *
 * Created: 4/18/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class OptionsScreen implements Screen {
    final JankyBlaster game;
    private Stage stage;
    Vector3 cursor;
    VisLabel label;

    public OptionsScreen(final JankyBlaster game) {
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
        table.align(Align.center | Align.bottom);
        table.columnDefaults(0).expandX().right().uniformX();
        table.columnDefaults(2).expandX().left().uniformX();

        // GAME OPTIONS
        label = new VisLabel("GAME OPTIONS");
        label.setAlignment(Align.left);
        table.add(label);
        table.row();
        // username
        table.add(new VisLabel("Username"));
        final VisTextField usernameField = new VisTextField(game.prefs.getString("username", "Guest" + (int )(Math.random() * 999 + 111)));
        usernameField.setMessageText("username");
        usernameField.setAlignment(Align.center);
        usernameField.setMaxLength(16);
        usernameField.setTextFieldListener(new VisTextField.TextFieldListener() {
            @Override
            public void keyTyped(VisTextField textField, char c) {
                game.prefs.putString("username", usernameField.getText());
            }
        });
        table.add(usernameField);
        table.row();
        // password
        table.add(new VisLabel("Password"));
        final VisTextField passwordField = new VisTextField(game.prefs.getString("password", ""));
        usernameField.setMessageText("password");
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        passwordField.setAlignment(Align.center);
        usernameField.setMaxLength(32);
        passwordField.setTextFieldListener(new VisTextField.TextFieldListener() {
            @Override
            public void keyTyped(VisTextField textField, char c) {
                game.prefs.putString("password", passwordField.getText());
            }
        });
        table.add(passwordField);
        table.row();
        table.addSeparator();


        // VIDEO OPTIONS
        label = new VisLabel("VIDEO OPTIONS");
        table.add(label);
        table.row().pad(3.0f);
        // contrast
        table.add(new Label("Contrast", VisUI.getSkin()));
        final Slider contrast = new Slider(0.1f, 1, 0.1f, false, VisUI.getSkin());
        contrast.setValue(game.prefs.getFloat("contrast", 0.5f));
        table.add(contrast);
        final Label contrastValue = new Label("" + game.prefs.getFloat("contrast", 0.5f), VisUI.getSkin());
        table.add(contrastValue);
        contrast.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.prefs.putFloat("contrast", contrast.getValue());
                contrastValue.setText("" + contrast.getValue());
            }
        });
        table.row();
        // brightness
        table.add(new VisLabel("Brightness"));
        final Slider brightness = new Slider(0.1f, 1, 0.1f, false, VisUI.getSkin());
        brightness.setValue(game.prefs.getFloat("brightness", 0.5f));
        table.add(brightness);
        final Label brightnessValue = new Label("" + game.prefs.getFloat("brightness", 0.5f), VisUI.getSkin());
        table.add(brightnessValue);
        brightness.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.prefs.putFloat("brightness", brightness.getValue());
                brightnessValue.setText("" + brightness.getValue());
            }
        });
        table.row();
        table.addSeparator();


        // SOUND OPTIONS
        label = new VisLabel("SOUND OPTIONS");
        table.add(label);
        table.row();
        // master volume
        table.add(new Label("Master Volume", VisUI.getSkin()));
        final Slider volume = new Slider(0.1f, 1, 0.1f, false, VisUI.getSkin());
        volume.setValue(game.prefs.getFloat("volume", 1.0f));
        table.add(volume);
        final Label volumeValue = new Label("" + game.prefs.getFloat("volume", 1.0f), VisUI.getSkin());
        table.add(volumeValue);
        volume.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.prefs.putFloat("volume", volume.getValue());
                volumeValue.setText("" + volume.getValue());
            }
        });
        table.row();
        // effects volume
        table.add(new VisLabel("Sound Effects Volume"));
        final Slider volumeEffects = new Slider(0.1f, 1, 0.1f, false, VisUI.getSkin());
        volumeEffects.setValue(game.prefs.getFloat("volumeEffects", 1.0f));
        table.add(volumeEffects);
        final Label volumeEffectsValue = new Label("" + game.prefs.getFloat("volumeEffects", 1.0f), VisUI.getSkin());
        table.add(volumeEffectsValue);
        volumeEffects.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.prefs.putFloat("volumeEffects", volumeEffects.getValue());
                volumeEffectsValue.setText("" + volumeEffects.getValue());
            }
        });
        table.row();
        // music volume
        table.add(new VisLabel("Music Volume"));
        final Slider volumeMusic = new Slider(0.1f, 1, 0.1f, false, VisUI.getSkin());
        volumeMusic.setValue(game.prefs.getFloat("volumeMusic", 1.0f));
        table.add(volumeMusic);
        final Label volumeMusicValue = new Label("" + game.prefs.getFloat("volumeMusic", 1.0f), VisUI.getSkin());
        table.add(volumeMusicValue);
        volumeMusic.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.prefs.putFloat("volumeMusic", volumeMusic.getValue());
                volumeMusicValue.setText("" + volumeMusic.getValue());
            }
        });
        table.row();
        table.addSeparator();


        // BUTTONS
        // save button
        final VisTextButton saveGameButton = new VisTextButton("SAVE SETTINGS");
        saveGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.prefs.flush();
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        table.add(saveGameButton);
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

        // CURSOR EFFECT
        Assets.engineParticle.getEmitters().first().setPosition(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
        Assets.engineParticle.start();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
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

        Assets.engineParticle.update(delta);
        Assets.engineParticle.getEmitters().first().durationTimer = 0;
        Assets.engineParticle.setPosition(cursor.x, cursor.y);
        game.batch.begin();
        Assets.engineParticle.draw(game.batch);
        game.batch.end();

        Gdx.gl.glBlendFunc(GL20.GL_ZERO, GL20.GL_SRC_COLOR);
        Gdx.gl.glBlendColor(game.prefs.getFloat("brightness"),game.prefs.getFloat("brightness"),game.prefs.getFloat("brightness"), 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width , height, false);
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
