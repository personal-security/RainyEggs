package com.xlab13.yummycarrot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.xlab13.yummycarrot.YummyCarrot;
import com.xlab13.yummycarrot.sprites.Button;


/**
 * Created by obitola on 12/24/2017.
 */

public class MenuState extends State {

    private Texture background;
    private Button playButton;
    //private Button leaderBoardButton;
    private Button optionsButton;
    private float scaleX;
    private float scaleY;
    private BitmapFont font, font2, font3;


    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, YummyCarrot.WIDTH, YummyCarrot.HEIGHT);
        playButton = new Button((cam.viewportWidth / 2), (cam.viewportHeight / 2),"playbutton.png");
        //leaderBoardButton = new Button((cam.viewportWidth / 2), (cam.viewportHeight * 3 / 8),"leaderboardbutton.png");
        optionsButton = new Button((cam.viewportWidth / 2), (cam.viewportHeight * 3 / 8),"optionsbutton.png");
        scaleX = cam.viewportWidth / Gdx.graphics.getWidth();
        scaleY = cam.viewportHeight / Gdx.graphics.getHeight();
        font = new BitmapFont(Gdx.files.internal("retro.fnt"));
        font.setColor(Color.valueOf("#7bc8e3"));
        font.getData().scale(0.1f);
        font2 = new BitmapFont(Gdx.files.internal("retro.fnt"));
        font2.setColor(Color.valueOf("#9c570e"));
        font2.getData().scale(0.5f);
        font3 = new BitmapFont(Gdx.files.internal("retro.fnt"));
        font3.setColor(Color.valueOf("#d77502"));
        font3.getData().scale(0.5f);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX() * scaleX;
            float y = YummyCarrot.HEIGHT - Gdx.input.getY() * scaleY;
            if (playButton.within(x, y)){
                gsm.game.getHandler().showBanner(false);
                gsm.set(new PlayState(gsm));
            }
            else if (optionsButton.within(x, y)){
                gsm.set(new OptionsState(gsm));
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
        sb.draw(playButton.getTexture(), playButton.getPosition().x, playButton.getPosition().y);
        //sb.draw(leaderBoardButton.getTexture(), leaderBoardButton.getPosition().x, leaderBoardButton.getPosition().y);
        sb.draw(optionsButton.getTexture(), optionsButton.getPosition().x, optionsButton.getPosition().y-25);
        font2.draw(sb, "YUMMY", YummyCarrot.WIDTH / 2 , YummyCarrot.HEIGHT * 30 / 32, Align.center, Align.center, true);
        font3.draw(sb, "CARROT", YummyCarrot.WIDTH / 2 , YummyCarrot.HEIGHT * 28 / 32, Align.center, Align.center, true);

        font.draw(sb, "SCORE: " + YummyCarrot.score, YummyCarrot.WIDTH / 2, YummyCarrot.HEIGHT * 12 / 16, Align.center, Align.center, true);
        font.draw(sb, "BEST: " + YummyCarrot.best, YummyCarrot.WIDTH / 2, YummyCarrot.HEIGHT * 11 / 16, Align.center, Align.center, true);
        sb.end();
    }

    @Override
    public void dispose() {
        //background.dispose();
        playButton.dispose();
        //leaderBoardButton.dispose();
        optionsButton.dispose();
        font2.dispose();
        font.dispose();
        System.out.println("Menu State Disposed");
    }
}
