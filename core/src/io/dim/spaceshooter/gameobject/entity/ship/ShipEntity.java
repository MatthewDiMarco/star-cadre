package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.Entity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public abstract class ShipEntity extends Entity {

    private static final float INVULNERABILITY_ALPHA_LOW = 0f;
    private static final float INVULNERABILITY_ALPHA_JUMP_RATE = 0.2f;

    public int hp;
    public int hpMax;

    public boolean lasersEnabled;
    public boolean laserArmourPiercing;
    public int laserStrength;
    public int laserPerShot;
    public float laserBarrelWidth;
    public float laserMovementSpeed;
    public float laserCooldownDuration;

    public boolean invulnerabilityEnabled;
    public float invulnerabilityDuration;

    public float timerLastLaser;
    public float timerLastHit;

    public float alpha;
    public TextureRegion shipTexture;
    public Sound laserSound;
    public Sound explosionSound;
    public Sound hurtSound;

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        this.timerLastLaser = timerLastLaser + deltaTime;
        this.timerLastHit = timerLastHit + deltaTime;
        if (invulnerabilityEnabled) {
            this.alpha = (alpha + INVULNERABILITY_ALPHA_JUMP_RATE) % 1;
            if (timerLastHit - invulnerabilityDuration >= 0) {
                invulnerabilityEnabled = false;
                alpha = 1f;
            }
        }
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        if (hitBox.y > 0) {
            explosionSound.play(1f);
            gameHandler.particleHandler.createExplosionEffect(
                hitBox.x + hitBox.width / 2,
                hitBox.y + hitBox.height / 2,
                Math.max(hitBox.width, hitBox.height) / 20);
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        Color cc = batch.getColor();
        batch.setColor(cc.r, cc.g, cc.b, alpha);
        batch.draw(
            shipTexture,
            hitBox.x,
            hitBox.y,
            hitBox.width,
            hitBox.height);
        batch.setColor(cc.r, cc.g, cc.b, 1f);
    }

    public void hit(int strength) {
        if (!invulnerabilityEnabled) {
            hp = Math.max(hp - strength, 0);
            if (hp == 0) {
                disposable = true;
            } else {
                hurtSound.play(1f);
            }
            invulnerabilityEnabled = true;
            timerLastHit = 0;
            alpha = INVULNERABILITY_ALPHA_LOW;
        }
    }

    public void fireLaser(GameHandler gameHandler) {
        boolean nextLaserReady = timerLastLaser - laserCooldownDuration >= 0;
        if (lasersEnabled && nextLaserReady) {
            laserSound.play(1.0f);
            timerLastLaser = 0;
            float laserRegionWidth = laserBarrelWidth / laserPerShot;
            for (int ii = 0; ii < laserPerShot; ii++) {
                float offset = (ii*laserRegionWidth + laserRegionWidth/2) - laserBarrelWidth/2;
                LaserEntity laser = this.getBaseLaser(gameHandler);
                laser.strength = laserStrength;
                laser.armourPiercing = laserArmourPiercing;
                laser.movementSpeed = laserMovementSpeed;
                laser.direction.x = offset;
                gameHandler.lasers.add(laser);
            }
        }
    }

    public abstract LaserEntity getBaseLaser(GameHandler gameHandler);
}
