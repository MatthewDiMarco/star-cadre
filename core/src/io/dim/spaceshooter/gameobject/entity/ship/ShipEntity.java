package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.Entity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public abstract class ShipEntity extends Entity {

    public static final float INVULNERABILITY_ALPHA_LOW = 0.25f;
    public static final float INVULNERABILITY_ALPHA_JUMP_RATE = 0.1f;

    public int hp;
    public int hpMax;
    public float firingCooldownDuration;
    public float invulnerabilityDuration;
    public boolean firingEnabled;
    public boolean invulnerabilityEnabled;

    protected float timerLastLaser;
    protected float timerLastHit;
    protected float alpha;
    protected final TextureRegion shipTexture;

    public ShipEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed, int hp,
        float firingCooldownDuration,
        float invulnerabilityDuration,
        TextureRegion shipTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.hp = hp;
        this.hpMax = hp;
        this.firingCooldownDuration = firingCooldownDuration;
        this.invulnerabilityDuration = invulnerabilityDuration;
        this.firingEnabled = Float.compare(firingCooldownDuration, 0f) != 0;
        this.invulnerabilityEnabled = false;
        this.timerLastLaser = 0;
        this.timerLastHit = 0;
        this.alpha = 1f;
        this.shipTexture = shipTexture;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        this.timerLastLaser = Math.min(firingCooldownDuration, timerLastLaser + deltaTime);
        this.timerLastHit = Math.min(invulnerabilityDuration, timerLastHit + deltaTime);
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
        gameHandler.particleHandler.createExplosionEffect(
            hitBox.x + hitBox.width / 2,
            hitBox.y + hitBox.height / 2,
            Math.max(hitBox.width, hitBox.height) / 10);
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

    public abstract void onFireLaser(GameHandler gameHandler);

    public void fireLaser(GameHandler gameHandler) {
        if (firingEnabled && timerLastLaser - firingCooldownDuration >= 0) {
            this.onFireLaser(gameHandler);
            timerLastLaser = 0;
        }
    }

    public void hit(LaserEntity laser) {
        if (!invulnerabilityEnabled) {
            laser.disposable = true;
            hp = Math.max(hp - laser.strength, 0);
            if (hp == 0) {
                disposable = true;
            }

            invulnerabilityEnabled = true;
            timerLastHit = 0;
            alpha = INVULNERABILITY_ALPHA_LOW;
        }
    }
}
