package com.xlab13.yummycarrot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.xlab13.yummycarrot.YummyCarrot;
import com.xlab13.yummycarrot.sprites.Button;

/**
 * Created by obitola on 12/31/2017.
 */

public class OptionsState extends State {

    private Button on;
    private Button off;
    private Button back;
    private float scaleX;
    private float scaleY;
    private BitmapFont font;
    private Preferences scores = Gdx.app.getPreferences(YummyCarrot.FILENAME);

    public OptionsState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, YummyCarrot.WIDTH, YummyCarrot.HEIGHT);
        on = new Button((cam.viewportWidth * 7 / 22), (cam.viewportHeight * 10 /16),"onbutton.png");
        off = new Button((cam.viewportWidth * 15 / 22), (cam.viewportHeight * 10 / 16),"offbutton.png");
        back = new Button((cam.viewportWidth * 1 / 2), (cam.viewportHeight * 1 / 4), "backbutton.png");
        scaleX = cam.viewportWidth / Gdx.graphics.getWidth();
        scaleY = cam.viewportHeight / Gdx.graphics.getHeight();
        font = new BitmapFont(Gdx.files.internal("retro.fnt"));
        font.setColor(Color.valueOf("#7bc8e3"));
        font.getData().scale(0.5f);

    }

    @Override
    protected void handleInput() {
        float x = Gdx.input.getX() * scaleX;
        float y = YummyCarrot.HEIGHT - Gdx.input.getY() * scaleY;
        if (Gdx.input.justTouched()) {
            if (back.within(x, y)){
                gsm.set(new MenuState(gsm));
            }
            else if(on.within(x, y)){
                scores.putBoolean("music", true);
                scores.flush();
                YummyCarrot.isSound = true;
                YummyCarrot.music.setVolume(0.1f);
            }
            else if(off.within(x, y)){
                scores.putBoolean("music", false);
                scores.flush();
                YummyCarrot.isSound = false;
                YummyCarrot.music.setVolume(0.0f);
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(on.getTexture(), on.getPosition().x, on.getPosition().y);
        sb.draw(off.getTexture(), off.getPosition().x, off.getPosition().y);
        sb.draw(back.getTexture(), back.getPosition().x, back.getPosition().y);
        font.draw(sb, "OPTIONS", YummyCarrot.WIDTH / 2 , YummyCarrot.HEIGHT * 13 / 16, Align.center, Align.center, true);
        //font.draw(sb, "TOTAL EGGS CAUGHT: " + RainyEggs.lifetime, RainyEggs.WIDTH / 2, RainyEggs.HEIGHT * 12 / 16, Align.center, Align.center, true);
        //font.draw(sb, "BEST: " + RainyEggs.best, RainyEggs.WIDTH / 2, RainyEggs.HEIGHT * 11 / 16, Align.center, Align.center, true);
       sb.end();

    }

    @Override
    public void dispose() {
        //background.dispose();
        on.dispose();
        off.dispose();
        back.dispose();
        font.dispose();
        System.out.println("Menu State Disposed");
    }
}
