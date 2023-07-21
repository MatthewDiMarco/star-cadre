package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.model.Entity;
import io.dim.spaceshooter.model.LaserEntity;
import io.dim.spaceshooter.model.EntityManager;

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

    public void hit(LaserEntity laser) {
        if (!invulnerable) {
            hp = Math.max(hp - laser.strength, 0);
            if (hp == 0) {
                disposable = true;
            }
            alpha = INVULNERABILITY_ALPHA_LOW;
        }
        invulnerable = true;
        timeSinceLastHit = 0;
    }

    @Override
    public void step(EntityManager entityManager, float deltaTime) {
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
    public void draw(SpriteBatch batch) {
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
}
