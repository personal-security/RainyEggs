package com.xlab13.rainyeggs.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.xlab13.rainyeggs.RainyEggsGame;
import com.xlab13.rainyeggs.sprites.Button;


/**
 * Created by obitola on 12/24/2017.
 */

public class MenuState extends State {

    //private Texture background;
    private Button playButton;
    //private Button leaderBoardButton;
    private Button optionsButton;
    private float scaleX;
    private float scaleY;
    private BitmapFont font;
    private BitmapFont font2;


    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, RainyEggsGame.WIDTH, RainyEggsGame.HEIGHT);
        //background = new Texture("bg.png");
        playButton = new Button((cam.viewportWidth / 2), (cam.viewportHeight / 2),"playbutton.png");
        //leaderBoardButton = new Button((cam.viewportWidth / 2), (cam.viewportHeight * 3 / 8),"leaderboardbutton.png");
        optionsButton = new Button((cam.viewportWidth / 2), (cam.viewportHeight * 3 / 8),"optionsbutton.png");
        scaleX = cam.viewportWidth / Gdx.graphics.getWidth();
        scaleY = cam.viewportHeight / Gdx.graphics.getHeight();
        font = new BitmapFont(Gdx.files.internal("hehe.fnt"));
        font2 = new BitmapFont(Gdx.files.internal("hehe2.fnt"));
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX() * scaleX;
            float y = RainyEggsGame.HEIGHT - Gdx.input.getY() * scaleY;
            if (playButton.within(x, y)){
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
        //sb.draw(background, 0, 0, FallingEggs.WIDTH, FallingEggs.HEIGHT);
        sb.draw(playButton.getTexture(), playButton.getPosition().x, playButton.getPosition().y);
        //sb.draw(leaderBoardButton.getTexture(), leaderBoardButton.getPosition().x, leaderBoardButton.getPosition().y);
        sb.draw(optionsButton.getTexture(), optionsButton.getPosition().x, optionsButton.getPosition().y);
        font2.draw(sb, "RAINY EGGS",RainyEggsGame.WIDTH / 2 , RainyEggsGame.HEIGHT * 7 / 8, Align.center, Align.center, true);
        font.draw(sb, "SCORE: " + RainyEggsGame.score, RainyEggsGame.WIDTH / 2, RainyEggsGame.HEIGHT * 12 / 16, Align.center, Align.center, true);
        font.draw(sb, "BEST: " + RainyEggsGame.best, RainyEggsGame.WIDTH / 2, RainyEggsGame.HEIGHT * 11 / 16, Align.center, Align.center, true);
        font.draw(sb, "LIFETIME EGGS", RainyEggsGame.WIDTH / 2, RainyEggsGame.HEIGHT * 2 / 16, Align.center, Align.center, true);
        font.draw(sb, String.valueOf(RainyEggsGame.lifetime), RainyEggsGame.WIDTH / 2, RainyEggsGame.HEIGHT * 1 / 16, Align.center, Align.center, true);

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
