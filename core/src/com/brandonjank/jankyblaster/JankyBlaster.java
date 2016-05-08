/*
 * JankyBlaster.java
 *
 * Created: 4/12/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 *
 * Many ideas for coding this came from the great libGDX wiki at https://github.com/libgdx/libgdx/wiki
 * Music is copyright M.O.O.N. http://music.musicofthemoon.com/album/moon-ep
 * All sounds were made by me. I used the audio synth at: http://www.bfxr.net
 * Graphics assets courtesy of Continuum
 *
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;

public class JankyBlaster extends Game {
	SpriteBatch batch;
	Preferences prefs;

	@Override
	public void create () {
		batch = new SpriteBatch();
		prefs = Gdx.app.getPreferences("JankyBlaster Preferences");

        // Load the VisUI styles
        VisUI.load();
        this.setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

    public void dispose() {
        batch.dispose();
        Assets.dispose();
    }

	@Override
	public void resume()
	{
		Assets.manager.finishLoading();
	}

}
