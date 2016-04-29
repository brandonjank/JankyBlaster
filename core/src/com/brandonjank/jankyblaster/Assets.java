package com.brandonjank.jankyblaster;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by jank6275 on 4/18/2016.
 */
public class Assets {
    // DECLARE MANAGER
    public static AssetManager manager;

    // DECLARE TEXTURES
    public static Texture shipTexture;
    public static final String shipTexturePath = "data/ship/ship.png";
    public static Texture shipLeftTexture;
    public static final String shipLeftTexturePath = "data/ship/shipleft.png";
    public static Texture shipRightTexture;
    public static final String shipRightTexturePath = "data/ship/shipright.png";
    public static Texture shipUpgradeTexture;
    public static final String shipUpgradeTexturePath = "data/ship/shipupgrade.png";

    public static Texture bulletTexture;
    public static final String bulletTexturePath = "data/ship/bullet.png";
    public static Texture enemyBulletTexture;
    public static final String enemyBulletTexturePath = "data/ship/enemybullet.png";
    public static Texture enemyBullet2Texture;
    public static final String enemyBullet2TexturePath = "data/ship/enemybullet2.png";
    public static Texture laserTexture;
    public static final String laserTexturePath = "data/ship/laser.png";

    public static Texture item1Texture;
    public static final String item1TexturePath = "data/ship/item1.png";
    public static Texture item2Texture;
    public static final String item2TexturePath = "data/ship/item2.png";
    public static Texture item3Texture;
    public static final String item3TexturePath = "data/ship/item3.png";
    public static Texture item4Texture;
    public static final String item4TexturePath = "data/ship/item4.png";
    public static Texture item5Texture;
    public static final String item5TexturePath = "data/ship/item5.png";
    public static Texture item6Texture;
    public static final String item6TexturePath = "data/ship/item6.png";

    public static Animation explosionRed;
    public static final String explosionRed1Path = "data/ship/explosionred01.png";
    public static final String explosionRed2Path = "data/ship/explosionred02.png";
    public static final String explosionRed3Path = "data/ship/explosionred03.png";
    public static final String explosionRed4Path = "data/ship/explosionred04.png";
    public static final String explosionRed5Path = "data/ship/explosionred05.png";
    public static final String explosionRed6Path = "data/ship/explosionred06.png";
    public static final String explosionRed7Path = "data/ship/explosionred07.png";
    public static final String explosionRed8Path = "data/ship/explosionred08.png";
    public static final String explosionRed9Path = "data/ship/explosionred09.png";
    public static final String explosionRed10Path = "data/ship/explosionred10.png";
    public static final String explosionRed11Path = "data/ship/explosionred11.png";

    public static Animation explosionBlue;
    public static final String explosionBlue1Path = "data/ship/explosionblue01.png";
    public static final String explosionBlue2Path = "data/ship/explosionblue02.png";
    public static final String explosionBlue3Path = "data/ship/explosionblue03.png";
    public static final String explosionBlue4Path = "data/ship/explosionblue04.png";
    public static final String explosionBlue5Path = "data/ship/explosionblue05.png";
    public static final String explosionBlue6Path = "data/ship/explosionblue06.png";
    public static final String explosionBlue7Path = "data/ship/explosionblue07.png";
    public static final String explosionBlue8Path = "data/ship/explosionblue08.png";
    public static final String explosionBlue9Path = "data/ship/explosionblue09.png";
    public static final String explosionBlue10Path = "data/ship/explosionblue10.png";
    public static final String explosionBlue11Path = "data/ship/explosionblue11.png";


    // DECLARE SOUNDS
    public static Sound alienSound;
    public static final String alienSoundPath = "data/sounds/alien.wav";
    public static Sound bossSound;
    public static final String bossSoundPath = "data/sounds/boss.wav";
    public static Sound boss2Sound;
    public static final String boss2SoundPath = "data/sounds/boss2.wav";
    public static Sound boss3Sound;
    public static final String boss3SoundPath = "data/sounds/boss3.wav";
    public static Sound boss4Sound;
    public static final String boss4SoundPath = "data/sounds/boss4.wav";
    public static Sound explodeSound;
    public static final String explodeSoundPath = "data/sounds/explode.wav";
    public static Sound explodeEnemySound;
    public static final String explodeEnemySoundPath = "data/sounds/explosion_enemy.wav";
    public static Sound explodePlayerSound;
    public static final String explodePlayerSoundPath = "data/sounds/explosion_player.wav";
    public static Sound failSound;
    public static final String failSoundPath = "data/sounds/fail.wav";
    public static Sound gameOverSound;
    public static final String gameOverSoundPath = "data/sounds/game_over.wav";
    public static Sound gameStartSound;
    public static final String gameStartSoundPath = "data/sounds/game_start.wav";
    public static Sound hitSound;
    public static final String hitSoundPath = "data/sounds/hit1.wav";
    public static Sound hit2Sound;
    public static final String hit2SoundPath = "data/sounds/hit2.wav";
    public static Sound hit3Sound;
    public static final String hit3SoundPath = "data/sounds/hit3.wav";
    public static Sound powerupAmmoSound;
    public static final String powerupAmmoSoundPath = "data/sounds/powerup_ammo.wav";
    public static Sound powerupHealthSound;
    public static final String powerupHealthSoundPath = "data/sounds/powerup_health.wav";
    public static Sound powerupSpeedSound;
    public static final String powerupSpeedSoundPath = "data/sounds/powerup_speed.wav";
    public static Sound powerupWeaponSound;
    public static final String powerupWeaponSoundPath = "data/sounds/powerup_weapon.wav";
    public static Sound shootBulletSound;
    public static final String shootBulletSoundPath = "data/sounds/shoot_bullet.wav";
    public static Sound shootBullet2Sound;
    public static final String shootBullet2SoundPath = "data/sounds/shoot_bullet2.wav";
    public static Sound shootLaserSound;
    public static final String shootLaserSoundPath = "data/sounds/shoot_laser.wav";
    public static Sound shootMissileSound;
    public static final String shootMissileSoundPath = "data/sounds/shoot_missile.wav";

    public static void create() {
        manager = new AssetManager();
        load();
    }

    public static void load() {
        // LOAD TEXTURES
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
    }

    public static void done() {
        // ASSIGN TEXTURES
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
    }

    public static void dispose() {
        manager.dispose();
    }
}
