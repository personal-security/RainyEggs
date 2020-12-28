package com.xlab13.yummycarrot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.xlab13.yummycarrot.YummyCarrot;
import com.xlab13.yummycarrot.sprites.Carrot;
import com.xlab13.yummycarrot.sprites.Ground;
import com.xlab13.yummycarrot.sprites.Player;

import java.util.Random;

import sun.rmi.runtime.Log;

import static com.xlab13.yummycarrot.YummyCarrot.loses;


/**
 * Created by obitola on 12/24/2017.
 */

public class PlayState extends State {

    private static final int START_HEIGHT = YummyCarrot.HEIGHT;
    private static final int SCORE_HEIGHT = 700;


    private Texture background, clouds;
    private float scaleX;
    private float scaleY;
    private BitmapFont font;
    private Vector2 position;
    private int score;
    private Array<Carrot> eggs;
    private Ground ground;
    private Player player;
    private Sound ochko, schopk1, schopk2, lose;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        eggs = new Array<Carrot>();
        eggs.add(new Carrot(START_HEIGHT));
        for (int i = 1; i < eggs.get(0).EGG_COUNT; i++){
            eggs.add(new Carrot(START_HEIGHT + (i * eggs.get(0).GAP)));
        }

        ground = new Ground();
        ochko = Gdx.audio.newSound(Gdx.files.internal("ochko1.wav"));
        schopk1 = Gdx.audio.newSound(Gdx.files.internal("chpok1.wav"));
        schopk2 = Gdx.audio.newSound(Gdx.files.internal("chpok2.wav"));
        lose = Gdx.audio.newSound(Gdx.files.internal("lose1.wav"));

        background = new Texture(Gdx.files.internal("bg.png"));
        clouds = new Texture(Gdx.files.internal("cloud.png"));
        player = new Player(YummyCarrot.WIDTH / 2, ground.getTexture().getHeight());
        cam.setToOrtho(false, YummyCarrot.WIDTH, YummyCarrot.HEIGHT);
        scaleX = cam.viewportWidth / Gdx.graphics.getWidth();
        scaleY = cam.viewportHeight / Gdx.graphics.getHeight();
        font = new BitmapFont(Gdx.files.internal("retro.fnt"));
        position = new Vector2(Gdx.input.getX() * scaleX, Gdx.input.getY() * scaleY);
        score = 0;
    }

    @Override
    protected void handleInput() {
        position.x = Gdx.input.getX() * scaleX;
        position.y = Gdx.input.getY() * scaleY;
    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(position.x, dt);
        for (int i = 0; i < eggs.size; i++) {
            Carrot egg = eggs.get(i);
            egg.update(dt);
            if (egg.isTouching(player.getHitBox())) {
                egg.reposition();
                score++;
                if (YummyCarrot.isSound) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (score % 50 == 0 && score > 0) ochko.play(0.2f);
                            else {
                                if (new Random().nextBoolean()) schopk1.play(0.2f);
                                else  schopk2.play(0.2f);
                            }
                        }
                    });
                    thread.start();
                }

            } else if (egg.getPosition().y <= ground.getTexture().getHeight()) {
                YummyCarrot.score = score;
                YummyCarrot.lifetime = YummyCarrot.lifetime + score;
                loses +=1;
                Preferences scores = Gdx.app.getPreferences(YummyCarrot.FILENAME);
                scores.putInteger("lifetime", YummyCarrot.lifetime);
                if (score > YummyCarrot.best) {
                    YummyCarrot.best = score;
                    scores.putInteger("best", score);
                }
                Gdx.app.debug("~~~", String.valueOf(loses));
                if (loses >= 5){
                    gsm.game.getHandler().showAds();
                    loses = 0;
                }
                scores.putInteger("loses", loses);
                if (YummyCarrot.isSound){
                    lose.play(0.2f);
                }
                scores.flush();
                gsm.set(new MenuState(gsm));
                gsm.game.getHandler().showBanner(true);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0, YummyCarrot.WIDTH, YummyCarrot.HEIGHT);
        if (YummyCarrot.WIDTH > ground.getHitbox().width) sb.draw(ground.getTexture(), 0, 0, YummyCarrot.WIDTH, ground.getTexture().getHeight());
        else sb.draw(ground.getTexture(), 0, 0);
        for (Carrot egg : eggs) {
            if (egg.getPosition().y <= YummyCarrot.HEIGHT-80) sb.draw(egg.getTexture(), egg.getPosition().x, egg.getPosition().y);
        }
        sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        font.draw(sb, Integer.toString(score), YummyCarrot.WIDTH / 2 , SCORE_HEIGHT-80, Align.center, Align.center, true);
        if (YummyCarrot.WIDTH > 468) sb.draw(clouds, 0, YummyCarrot.HEIGHT-80, YummyCarrot.WIDTH, YummyCarrot.HEIGHT);
        else sb.draw(clouds, 0, YummyCarrot.HEIGHT-80);
        sb.end();
    }

    @Override
    public void dispose() {
//        background.dispose();
        font.dispose();
        for (Carrot egg : eggs){
            egg.dispose();
        }
        ground.dispose();
        player.dispose();
        ochko.dispose();
        schopk1.dispose();
        schopk2.dispose();
        lose.dispose();
    }
}
