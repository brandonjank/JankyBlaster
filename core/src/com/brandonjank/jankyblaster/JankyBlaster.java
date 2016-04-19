package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;

public class JankyBlaster extends Game {
    BitmapFont font;
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
        font = new BitmapFont();
        // Load the VisUI styles
        VisUI.load();
        this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

	@Override
	public void resume()
	{
	}

}
