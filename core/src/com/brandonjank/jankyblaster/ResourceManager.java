package com.brandonjank.jankyblaster;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by backb on 4/18/2016.
 */
public class ResourceManager {
    public static AssetManager manager;
    //public static TextureAtlas myGraphics;
    //public static TextureAtlas myOtherGraphics;
    // ship
    public static Texture shipTexture;
    public static final String shipTexturePath = "data/ship/ship.png";
    // shipleft
    public static Texture shipLeftTexture;
    public static final String shipLeftTexturePath = "data/ship/shipleft.png";
    // shipright
    public static Texture shipRightTexture;
    public static final String shipRightTexturePath = "data/ship/shipright.png";

    public static void create() {
        manager = new AssetManager();
        load();
    }

    private static void load() {
        // manager.load("mygraphics.pack", TextureAtlas.class);
        // manager.load("myothergraphics.pack", TextureAtlas.class);

        // load the segment textures
        manager.load(shipTexturePath, Texture.class);
        manager.load(shipLeftTexturePath, Texture.class);
        manager.load(shipRightTexturePath, Texture.class);

        while(!manager.update())
        {
            System.out.println("Loaded: " + manager.getProgress() *100 + "%");
        }
    }

    public static void done() {
        // myGraphics = manager.get("mygraphics.pack", TextureAtlas.class);
        // myOtherGraphics = manager.get("myothergraphics.pack", TextureAtlas.class);
        shipTexture = manager.get(shipTexturePath);
        shipLeftTexture = manager.get(shipLeftTexturePath);
        shipRightTexture = manager.get(shipRightTexturePath);
    }

    public static void dispose() {
        manager.dispose();
        manager = null;
    }

    public static Boolean isLoaded()
    {
        if(manager.getProgress() >= 1) {
            return true;
        }

        return false;
    }
}
