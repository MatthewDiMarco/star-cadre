package io.dim.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class ParticleManager {

    public ParticleEffectPool laserCombustionBluePool;
    public ParticleEffectPool laserCombustionRedPool;
    public Array<PooledEffect> effects;

    public ParticleManager(TextureAtlas atlas) {
        this.effects = new Array<>();

        ParticleEffect laserCombustionBlueEffect = new ParticleEffect();
        laserCombustionBlueEffect.load(Gdx.files.internal(
            "laserCombustionBlue.party"), atlas);
        laserCombustionBlueEffect.scaleEffect(0.1f);

        ParticleEffect laserCombustionRedEffect = new ParticleEffect();
        laserCombustionRedEffect.load(Gdx.files.internal(
            "laserCombustionRed.party"), atlas);
        laserCombustionRedEffect.scaleEffect(0.1f);

        laserCombustionBluePool = new ParticleEffectPool(
            laserCombustionBlueEffect, 1, 2);
        laserCombustionRedPool = new ParticleEffectPool(
            laserCombustionRedEffect, 1, 2);
    }

    public void updateParticles(float deltaTime) {
        for (int ii = 0; ii < effects.size; ii++) {
            PooledEffect effect = effects.get(ii);
            effect.update(deltaTime);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(ii);
            }
        }
    }

    public void drawParticles(SpriteBatch batch) {
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

    private void createEffect(PooledEffect effect, float xx, float yy) {
        effect.setPosition(xx, yy);
        effects.add(effect);
    }

}
