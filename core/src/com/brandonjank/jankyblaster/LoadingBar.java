/*
 * LoadingBar.java
 *
 * Created: 4/28/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoadingBar extends Actor {

    Animation animation;
    TextureRegion reg;
    float stateTime;

    public LoadingBar (Animation animation) {
        this.animation = animation;
        reg = animation.getKeyFrame(0);
    }

    @Override
    public void act (float delta) {
        stateTime += delta;
        reg = animation.getKeyFrame(stateTime);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(reg, getX(), getY());
    }
}

