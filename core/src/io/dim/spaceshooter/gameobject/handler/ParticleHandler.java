package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import io.dim.spaceshooter.gameobject.GameObject;

public class ParticleHandler implements GameObject {

    public ParticleEffectPool laserSmokeBluePool;
    public ParticleEffectPool laserSmokeRedPool;
    public ParticleEffectPool explosionPool;
    public ParticleEffectPool smokePool;
    public Array<PooledEffect> effects;

    public ParticleHandler(TextureAtlas atlas) {
        this.effects = new Array<>();

        ParticleEffect laserSmokeBlueEffect = new ParticleEffect();
        laserSmokeBlueEffect.load(Gdx.files.internal(
            "effects/laserSmokeBlue.party"), atlas);
        laserSmokeBlueEffect.scaleEffect(0.1f);

        ParticleEffect laserSmokeRedEffect = new ParticleEffect();
        laserSmokeRedEffect.load(Gdx.files.internal(
            "effects/laserSmokeRed.party"), atlas);
        laserSmokeRedEffect.scaleEffect(0.1f);

        ParticleEffect explosionEffect = new ParticleEffect();
        explosionEffect.load(Gdx.files.internal(
            "effects/explosion.party"), atlas);
        explosionEffect.scaleEffect(0.2f);

        ParticleEffect smokeEffect = new ParticleEffect();
        smokeEffect.load(Gdx.files.internal(
            "effects/smoke.party"), atlas);
        smokeEffect.scaleEffect(0.125f);

        laserSmokeBluePool = new ParticleEffectPool(
            laserSmokeBlueEffect, 1, 5);
        laserSmokeRedPool = new ParticleEffectPool(
            laserSmokeRedEffect, 1, 5);
        explosionPool = new ParticleEffectPool(
            explosionEffect, 1, 5);
        smokePool = new ParticleEffectPool(
            smokeEffect, 1, 5);
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
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
    public void onDraw(SpriteBatch batch) {
        for (PooledEffect effect : effects) effect.draw(batch);
    }

    public void createLaserSmokeBlueEffect(float xx, float yy) {
        PooledEffect effect = laserSmokeBluePool.obtain();
        createEffect(effect, xx, yy);
    }

    public void createLaserSmokeRedEffect(float xx, float yy) {
        PooledEffect effect = laserSmokeRedPool.obtain();
        createEffect(effect, xx, yy);
    }

    public void createExplosionEffect(float xx, float yy, float scale) {
        PooledEffect smokeEffect = explosionPool.obtain();
        smokeEffect.scaleEffect(scale);
        createEffect(smokeEffect, xx, yy);
    }

    private void createEffect(PooledEffect effect, float xx, float yy) {
        effect.setPosition(xx, yy);
        effects.add(effect);
    }

}
