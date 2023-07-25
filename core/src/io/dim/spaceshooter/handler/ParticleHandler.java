package io.dim.spaceshooter.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import io.dim.spaceshooter.ApplicationObject;

public class ParticleHandler implements ApplicationObject {

    public ParticleEffectPool laserCombustionBluePool;
    public ParticleEffectPool laserCombustionRedPool;
    public ParticleEffectPool explosionPool;
    public Array<PooledEffect> effects;

    public ParticleHandler(TextureAtlas atlas) {
        this.effects = new Array<>();

        ParticleEffect laserCombustionBlueEffect = new ParticleEffect();
        laserCombustionBlueEffect.load(Gdx.files.internal(
            "effects/laserCombustionBlue.party"), atlas);
        laserCombustionBlueEffect.scaleEffect(0.1f);

        ParticleEffect laserCombustionRedEffect = new ParticleEffect();
        laserCombustionRedEffect.load(Gdx.files.internal(
            "effects/laserCombustionRed.party"), atlas);
        laserCombustionRedEffect.scaleEffect(0.1f);

        ParticleEffect explosionEffect = new ParticleEffect();
        explosionEffect.load(Gdx.files.internal(
            "effects/explosion.party"), atlas);
        explosionEffect.scaleEffect(0.2f);

        laserCombustionBluePool = new ParticleEffectPool(
            laserCombustionBlueEffect, 1, 2);
        laserCombustionRedPool = new ParticleEffectPool(
            laserCombustionRedEffect, 1, 2);
        explosionPool = new ParticleEffectPool(
            explosionEffect, 1, 2);
    }

    @Override
    public void update(float deltaTime) {
        for (int ii = 0; ii < effects.size; ii++) {
            PooledEffect effect = effects.get(ii);
            effect.update(deltaTime);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(ii);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (PooledEffect effect : effects) effect.draw(batch);
    }

    public void createLaserBlueEffect(float xx, float yy) {
        PooledEffect effect = laserCombustionBluePool.obtain();
        createEffect(effect, xx, yy);
    }

    public void createLaserRedEffect(float xx, float yy) {
        PooledEffect effect = laserCombustionRedPool.obtain();
        createEffect(effect, xx, yy);
    }

    public void createExplosionEffect(float xx, float yy, float scale) {
        PooledEffect effect = explosionPool.obtain();
        effect.scaleEffect(scale);
        createEffect(effect, xx, yy);
    }

    private void createEffect(PooledEffect effect, float xx, float yy) {
        effect.setPosition(xx, yy);
        effects.add(effect);
    }

}
