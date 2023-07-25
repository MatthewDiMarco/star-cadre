package io.dim.spaceshooter.entity.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.entity.Entity;
import io.dim.spaceshooter.entity.LaserEntity;
import io.dim.spaceshooter.handler.EntityHandler;
import io.dim.spaceshooter.handler.ParticleHandler;

public abstract class ShipEntity extends Entity {

    public static final float INVULNERABILITY_ALPHA_LOW = 0.25f;
    public static final float INVULNERABILITY_ALPHA_JUMP_RATE = 0.1f;

    public int hp;
    public int hpMax;
    public float fireRate;
    public boolean invulnerable;
    public float invulnerabilityTime;

    protected float timeSinceLastShot;
    protected float timeSinceLastHit;
    protected float alpha;
    protected final TextureRegion shipTexture;

    public ShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int hp, float invulnerabilityTime, float fireRate,
        TextureRegion shipTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.hp = hp;
        this.hpMax = hp;
        this.fireRate = fireRate;
        this.invulnerable = false;
        this.invulnerabilityTime = invulnerabilityTime;
        this.timeSinceLastShot = 0;
        this.timeSinceLastHit = 0;
        this.alpha = 1f;
        this.shipTexture = shipTexture;
    }

    @Override
    public void onStep(EntityHandler entityHandler, float deltaTime) {
        this.timeSinceLastShot = Math.min(fireRate, timeSinceLastShot + deltaTime);
        this.timeSinceLastHit = Math.min(invulnerabilityTime, timeSinceLastHit + deltaTime);

        if (invulnerable) {
            this.alpha = (alpha + INVULNERABILITY_ALPHA_JUMP_RATE) % 1;
            if (timeSinceLastHit - invulnerabilityTime >= 0) {
                invulnerable = false;
                alpha = 1f;
            }
        }
    }

    @Override
    public void onDestroy(EntityHandler entityHandler, ParticleHandler particleHandler) {
        particleHandler.createExplosionEffect(
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

    public abstract void onFireLaser(EntityHandler entityHandler);

    public void fireLaser(EntityHandler entityHandler) {
        if (timeSinceLastShot - fireRate >= 0) {
            this.onFireLaser(entityHandler);
            timeSinceLastShot = 0;
        }
    }

    public void hit(LaserEntity laser) {
        if (!invulnerable) {
            laser.disposable = true;
            hp = Math.max(hp - laser.strength, 0);
            if (hp == 0) {
                disposable = true;
            }

            invulnerable = true;
            timeSinceLastHit = 0;
            alpha = INVULNERABILITY_ALPHA_LOW;
        }
    }
}
