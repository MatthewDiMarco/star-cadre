package io.dim.spaceshooter.helper;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Assets {

    public TextureAtlas textureAtlas;
    public Texture restartTexture;
    public Texture[] backgrounds = new Texture[3];
    public FreeTypeFontGenerator fontGenerator;
    public Sound laserSound1;
    public Sound laserSound2;
    public Sound explosionSound1;
    public Sound hurtSound1;
}
