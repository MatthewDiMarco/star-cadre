package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.model.Entity;
import io.dim.spaceshooter.model.Laser;
import io.dim.spaceshooter.model.World;

public abstract class ShipEntity extends Entity {

    public static final float INVLUNERABILITY_TIME = 0.5f;

    public int hp;
    public int hpMax;
    public float fireRate;
    public boolean functional;
    public boolean invulnerable;
    public float alpha;

    protected float timeSinceLastShot;
    protected float timeSinceLastHit;
    protected final TextureRegion shipTexture;

    public ShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int hp, float fireRate, TextureRegion shipTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.hp = hp;
        this.hpMax = hp;
        this.fireRate = fireRate;
        this.functional = true;
        this.invulnerable = false;
        this.alpha = 1f;
        this.timeSinceLastShot = 0;
        this.timeSinceLastHit = 0;
        this.shipTexture = shipTexture;
    }

    public void hit(Laser laser) {
        if (!invulnerable && functional) {
            alpha = 0.25f;
            hp = Math.max(hp - laser.strength, 0);
            if (hp == 0) {
                functional = false;
            }
        }
    }

    @Override
    public void step(World world, float deltaTime) {
        this.timeSinceLastShot = Math.min(fireRate, timeSinceLastShot + deltaTime);
        this.timeSinceLastHit = Math.min(INVLUNERABILITY_TIME, timeSinceLastHit + deltaTime);
        if (timeSinceLastHit == INVLUNERABILITY_TIME) invulnerable = false;
        if (functional) this.alpha = Math.min(1f, alpha + 0.1f);
        if (invulnerable) this.alpha = (alpha + 0.1f) % 1;
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
