package com.brandonjank.jankyblaster;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

/**
 * Created by Phelps on 5/8/2016.
 */
public class SpaceMineManager {

    private GameScreen game;
    public ArrayList<SpaceMine> mines = new ArrayList<SpaceMine>();

    public SpaceMineManager(GameScreen game) {
        this.game = game;
    }

    public SpaceMine placeMine(float x, float y, boolean isMine, String owner, String p) {
        for (SpaceMine m: mines) {
            if (m.free) {
                m.free = false;
                m.isMine = isMine;
                m.owner = owner;
                m.player = p;
                return m;
            }
        }

        Gdx.app.log("SpaceMineManager", "Size is now " + mines.size());
        SpaceMine m = new SpaceMine(game);
        m.isMine = isMine;
        System.out.println(x + " " + y);
        m.place(x, y);
        mines.add(m);
        return m;
    }

}
