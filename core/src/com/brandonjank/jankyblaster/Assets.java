/*
 * Assets.java
 *
 * Created: 4/18/2016
 * Author : Brandon Jank <jank6275@vandals.uidaho.edu>
 */

package com.brandonjank.jankyblaster;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

class Assets {
    // DECLARE MANAGER
    static AssetManager manager;

    // DECLARE FONTS
    static BitmapFont consolas8Font;
    private static final String consolas8FontPath = "data/fonts/consolas8.fnt";
    static BitmapFont consolas10Font;
    private static final String consolas10FontPath = "data/fonts/consolas10.fnt";
    static BitmapFont consolas12Font;
    private static final String consolas12FontPath = "data/fonts/consolas12.fnt";
    static BitmapFont consolas14Font;
    private static final String consolas14FontPath = "data/fonts/consolas14.fnt";
    static BitmapFont consolas16Font;
    private static final String consolas16FontPath = "data/fonts/consolas16.fnt";
    static BitmapFont consolas32Font;
    private static final String consolas32FontPath = "data/fonts/consolas32.fnt";

    // DECLARE TEXTURES
    static Texture backgroundTexture;
    private static final String backgroundTexturePath = "data/background.png";
    static Texture background2Texture;
    private static final String background2TexturePath = "data/background2.png";
    static Texture menuBackgroundTexture;
    private static final String menuBackgroundTexturePath = "data/menubg.png";

    static Texture shipTexture;
    private static final String shipTexturePath = "data/ship/ship.png";
    static Texture shipLeftTexture;
    private static final String shipLeftTexturePath = "data/ship/shipleft.png";
    static Texture shipRightTexture;
    private static final String shipRightTexturePath = "data/ship/shipright.png";
    static Texture shipUpgradeTexture;
    private static final String shipUpgradeTexturePath = "data/ship/shipupgrade.png";

    static Texture bulletTexture;
    private static final String bulletTexturePath = "data/ship/bullet.png";
    static Texture enemyBulletTexture;
    private static final String enemyBulletTexturePath = "data/ship/enemybullet.png";
    static Texture enemyBullet2Texture;
    private static final String enemyBullet2TexturePath = "data/ship/enemybullet2.png";
    static Texture laserTexture;
    private static final String laserTexturePath = "data/ship/laser.png";

    static Texture item1Texture;
    private static final String item1TexturePath = "data/ship/item1.png";
    static Texture item2Texture;
    private static final String item2TexturePath = "data/ship/item2.png";
    static Texture item3Texture;
    private static final String item3TexturePath = "data/ship/item3.png";
    static Texture item4Texture;
    private static final String item4TexturePath = "data/ship/item4.png";
    static Texture item5Texture;
    private static final String item5TexturePath = "data/ship/item5.png";
    static Texture item6Texture;
    private static final String item6TexturePath = "data/ship/item6.png";

    static Animation explosionRed;
    private static final String explosionRed1Path = "data/ship/explosionred01.png";
    private static final String explosionRed2Path = "data/ship/explosionred02.png";
    private static final String explosionRed3Path = "data/ship/explosionred03.png";
    private static final String explosionRed4Path = "data/ship/explosionred04.png";
    private static final String explosionRed5Path = "data/ship/explosionred05.png";
    private static final String explosionRed6Path = "data/ship/explosionred06.png";
    private static final String explosionRed7Path = "data/ship/explosionred07.png";
    private static final String explosionRed8Path = "data/ship/explosionred08.png";
    private static final String explosionRed9Path = "data/ship/explosionred09.png";
    private static final String explosionRed10Path = "data/ship/explosionred10.png";
    private static final String explosionRed11Path = "data/ship/explosionred11.png";

    static Animation explosionBlue;
    private static final String explosionBlue1Path = "data/ship/explosionblue01.png";
    private static final String explosionBlue2Path = "data/ship/explosionblue02.png";
    private static final String explosionBlue3Path = "data/ship/explosionblue03.png";
    private static final String explosionBlue4Path = "data/ship/explosionblue04.png";
    private static final String explosionBlue5Path = "data/ship/explosionblue05.png";
    private static final String explosionBlue6Path = "data/ship/explosionblue06.png";
    private static final String explosionBlue7Path = "data/ship/explosionblue07.png";
    private static final String explosionBlue8Path = "data/ship/explosionblue08.png";
    private static final String explosionBlue9Path = "data/ship/explosionblue09.png";
    private static final String explosionBlue10Path = "data/ship/explosionblue10.png";
    private static final String explosionBlue11Path = "data/ship/explosionblue11.png";

    static Texture controlsTexture;
    private static final String controlsTexturePath = "data/overlays/controls.png";


    // DECLARE SOUNDS
    static Sound alienSound;
    private static final String alienSoundPath = "data/sounds/alien.wav";
    static Sound bossSound;
    private static final String bossSoundPath = "data/sounds/boss.wav";
    static Sound boss2Sound;
    private static final String boss2SoundPath = "data/sounds/boss2.wav";
    static Sound boss3Sound;
    private static final String boss3SoundPath = "data/sounds/boss3.wav";
    static Sound boss4Sound;
    private static final String boss4SoundPath = "data/sounds/boss4.wav";
    static Sound explodeSound;
    private static final String explodeSoundPath = "data/sounds/explode.wav";
    static Sound explodeEnemySound;
    private static final String explodeEnemySoundPath = "data/sounds/explosion_enemy.wav";
    static Sound explodePlayerSound;
    private static final String explodePlayerSoundPath = "data/sounds/explosion_player.wav";
    static Sound failSound;
    private static final String failSoundPath = "data/sounds/fail.wav";
    static Sound gameOverSound;
    private static final String gameOverSoundPath = "data/sounds/game_over.wav";
    static Sound gameStartSound;
    private static final String gameStartSoundPath = "data/sounds/game_start.wav";
    static Sound hitSound;
    private static final String hitSoundPath = "data/sounds/hit1.wav";
    static Sound hit2Sound;
    private static final String hit2SoundPath = "data/sounds/hit2.wav";
    static Sound hit3Sound;
    private static final String hit3SoundPath = "data/sounds/hit3.wav";
    static Sound powerupAmmoSound;
    private static final String powerupAmmoSoundPath = "data/sounds/powerup_ammo.wav";
    static Sound powerupHealthSound;
    private static final String powerupHealthSoundPath = "data/sounds/powerup_health.wav";
    static Sound powerupSpeedSound;
    private static final String powerupSpeedSoundPath = "data/sounds/powerup_speed.wav";
    static Sound powerupWeaponSound;
    private static final String powerupWeaponSoundPath = "data/sounds/powerup_weapon.wav";
    static Sound shootBulletSound;
    private static final String shootBulletSoundPath = "data/sounds/shoot_bullet.wav";
    static Sound shootBullet2Sound;
    private static final String shootBullet2SoundPath = "data/sounds/shoot_bullet2.wav";
    static Sound shootLaserSound;
    private static final String shootLaserSoundPath = "data/sounds/shoot_laser.wav";
    static Sound shootMissileSound;
    private static final String shootMissileSoundPath = "data/sounds/shoot_missile.wav";

    // declare particle effects
    static ParticleEffect engineParticle;
    private static final String engineParticlePath = "data/particles/engine.p";

    static void create() {
        manager = new AssetManager();
        load();
    }

    private static void load() {
        // LOAD FONTS
        manager.load(consolas8FontPath, BitmapFont.class);
        manager.load(consolas10FontPath, BitmapFont.class);
        manager.load(consolas12FontPath, BitmapFont.class);
        manager.load(consolas14FontPath, BitmapFont.class);
        manager.load(consolas16FontPath, BitmapFont.class);
        manager.load(consolas32FontPath, BitmapFont.class);

        // LOAD TEXTURES
        manager.load(backgroundTexturePath, Texture.class);
        manager.load(background2TexturePath, Texture.class);
        manager.load(menuBackgroundTexturePath, Texture.class);
        manager.load(shipTexturePath, Texture.class);
        manager.load(shipLeftTexturePath, Texture.class);
        manager.load(shipRightTexturePath, Texture.class);
        manager.load(bulletTexturePath, Texture.class);
        manager.load(shipUpgradeTexturePath, Texture.class);
        manager.load(enemyBulletTexturePath, Texture.class);
        manager.load(enemyBullet2TexturePath, Texture.class);
        manager.load(laserTexturePath, Texture.class);
        manager.load(item1TexturePath, Texture.class);
        manager.load(item2TexturePath, Texture.class);
        manager.load(item3TexturePath, Texture.class);
        manager.load(item4TexturePath, Texture.class);
        manager.load(item5TexturePath, Texture.class);
        manager.load(item6TexturePath, Texture.class);
        manager.load(explosionRed1Path, Texture.class);
        manager.load(explosionRed2Path, Texture.class);
        manager.load(explosionRed3Path, Texture.class);
        manager.load(explosionRed4Path, Texture.class);
        manager.load(explosionRed5Path, Texture.class);
        manager.load(explosionRed6Path, Texture.class);
        manager.load(explosionRed7Path, Texture.class);
        manager.load(explosionRed8Path, Texture.class);
        manager.load(explosionRed9Path, Texture.class);
        manager.load(explosionRed10Path, Texture.class);
        manager.load(explosionRed11Path, Texture.class);
        manager.load(explosionBlue1Path, Texture.class);
        manager.load(explosionBlue2Path, Texture.class);
        manager.load(explosionBlue3Path, Texture.class);
        manager.load(explosionBlue4Path, Texture.class);
        manager.load(explosionBlue5Path, Texture.class);
        manager.load(explosionBlue6Path, Texture.class);
        manager.load(explosionBlue7Path, Texture.class);
        manager.load(explosionBlue8Path, Texture.class);
        manager.load(explosionBlue9Path, Texture.class);
        manager.load(explosionBlue10Path, Texture.class);
        manager.load(explosionBlue11Path, Texture.class);
        manager.load(controlsTexturePath, Texture.class);

        // LOAD SOUNDS
        manager.load(alienSoundPath, Sound.class);
        manager.load(bossSoundPath, Sound.class);
        manager.load(boss2SoundPath, Sound.class);
        manager.load(boss3SoundPath, Sound.class);
        manager.load(boss4SoundPath, Sound.class);
        manager.load(explodeSoundPath, Sound.class);
        manager.load(explodeEnemySoundPath, Sound.class);
        manager.load(explodePlayerSoundPath, Sound.class);
        manager.load(failSoundPath, Sound.class);
        manager.load(gameOverSoundPath, Sound.class);
        manager.load(gameStartSoundPath, Sound.class);
        manager.load(hitSoundPath, Sound.class);
        manager.load(hit2SoundPath, Sound.class);
        manager.load(hit3SoundPath, Sound.class);
        manager.load(powerupAmmoSoundPath, Sound.class);
        manager.load(powerupHealthSoundPath, Sound.class);
        manager.load(powerupSpeedSoundPath, Sound.class);
        manager.load(powerupWeaponSoundPath, Sound.class);
        manager.load(shootBulletSoundPath, Sound.class);
        manager.load(shootBullet2SoundPath, Sound.class);
        manager.load(shootLaserSoundPath, Sound.class);
        manager.load(shootMissileSoundPath, Sound.class);

        // load particles
        manager.load(engineParticlePath, ParticleEffect.class);
    }

    static void done() {
        // ASSIGN FONTS
        consolas8Font = manager.get(consolas8FontPath);
        consolas10Font = manager.get(consolas10FontPath);
        consolas12Font = manager.get(consolas12FontPath);
        consolas14Font = manager.get(consolas14FontPath);
        consolas16Font = manager.get(consolas16FontPath);
        consolas32Font = manager.get(consolas32FontPath);

        // ASSIGN TEXTURES
        backgroundTexture = manager.get(backgroundTexturePath);
        background2Texture = manager.get(background2TexturePath);
        menuBackgroundTexture = manager.get(menuBackgroundTexturePath);
        shipTexture = manager.get(shipTexturePath);
        shipLeftTexture = manager.get(shipLeftTexturePath);
        shipRightTexture = manager.get(shipRightTexturePath);
        bulletTexture = manager.get(bulletTexturePath);
        shipUpgradeTexture = manager.get(shipUpgradeTexturePath);
        enemyBulletTexture = manager.get(enemyBulletTexturePath);
        enemyBullet2Texture = manager.get(enemyBullet2TexturePath);
        laserTexture = manager.get(laserTexturePath);
        item1Texture = manager.get(item1TexturePath);
        item2Texture = manager.get(item2TexturePath);
        item3Texture = manager.get(item3TexturePath);
        item4Texture = manager.get(item4TexturePath);
        item5Texture = manager.get(item5TexturePath);
        item6Texture = manager.get(item6TexturePath);
        explosionRed = new Animation(
                0.05f,
                new TextureRegion((Texture) manager.get(explosionRed1Path)),
                new TextureRegion((Texture) manager.get(explosionRed2Path)),
                new TextureRegion((Texture) manager.get(explosionRed3Path)),
                new TextureRegion((Texture) manager.get(explosionRed4Path)),
                new TextureRegion((Texture) manager.get(explosionRed5Path)),
                new TextureRegion((Texture) manager.get(explosionRed6Path)),
                new TextureRegion((Texture) manager.get(explosionRed7Path)),
                new TextureRegion((Texture) manager.get(explosionRed8Path)),
                new TextureRegion((Texture) manager.get(explosionRed9Path)),
                new TextureRegion((Texture) manager.get(explosionRed10Path)),
                new TextureRegion((Texture) manager.get(explosionRed11Path))
        );
        explosionBlue = new Animation(
                0.05f,
                new TextureRegion((Texture) manager.get(explosionBlue1Path)),
                new TextureRegion((Texture) manager.get(explosionBlue2Path)),
                new TextureRegion((Texture) manager.get(explosionBlue3Path)),
                new TextureRegion((Texture) manager.get(explosionBlue4Path)),
                new TextureRegion((Texture) manager.get(explosionBlue5Path)),
                new TextureRegion((Texture) manager.get(explosionBlue6Path)),
                new TextureRegion((Texture) manager.get(explosionBlue7Path)),
                new TextureRegion((Texture) manager.get(explosionBlue8Path)),
                new TextureRegion((Texture) manager.get(explosionBlue9Path)),
                new TextureRegion((Texture) manager.get(explosionBlue10Path)),
                new TextureRegion((Texture) manager.get(explosionBlue11Path))
        );
        controlsTexture = manager.get(controlsTexturePath);

        // ASSIGN SOUNDS
        alienSound = manager.get(alienSoundPath);
        bossSound = manager.get(bossSoundPath);
        boss2Sound = manager.get(boss2SoundPath);
        boss3Sound = manager.get(boss3SoundPath);
        boss4Sound = manager.get(boss4SoundPath);
        explodeSound = manager.get(explodeSoundPath);
        explodeEnemySound = manager.get(explodeEnemySoundPath);
        explodePlayerSound = manager.get(explodePlayerSoundPath);
        failSound = manager.get(failSoundPath);
        gameOverSound = manager.get(gameOverSoundPath);
        gameStartSound = manager.get(gameStartSoundPath);
        hitSound = manager.get(hitSoundPath);
        hit2Sound = manager.get(hit2SoundPath);
        hit3Sound = manager.get(hit3SoundPath);
        powerupAmmoSound = manager.get(powerupAmmoSoundPath);
        powerupHealthSound = manager.get(powerupHealthSoundPath);
        powerupSpeedSound = manager.get(powerupSpeedSoundPath);
        powerupWeaponSound = manager.get(powerupWeaponSoundPath);
        shootBulletSound = manager.get(shootBulletSoundPath);
        shootBullet2Sound = manager.get(shootBullet2SoundPath);
        shootLaserSound = manager.get(shootLaserSoundPath);
        shootMissileSound = manager.get(shootMissileSoundPath);

        // ASSIGN PARTICLES
        engineParticle = manager.get(engineParticlePath);
    }

    static void dispose() {
        manager.dispose();
    }
}
