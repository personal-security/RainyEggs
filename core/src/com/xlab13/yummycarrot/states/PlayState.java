package com.xlab13.yummycarrot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
    private boolean isSound;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        eggs = new Array<Carrot>();
        eggs.add(new Carrot(START_HEIGHT));
        for (int i = 1; i < eggs.get(0).EGG_COUNT; i++){
            eggs.add(new Carrot(START_HEIGHT + (i * eggs.get(0).GAP)));
        }

        ground = new Ground();

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
            } else if (egg.getPosition().y <= ground.getTexture().getHeight()) {
                YummyCarrot.score = score;
                YummyCarrot.lifetime = YummyCarrot.lifetime + score;
                YummyCarrot.loses +=1;
                Preferences scores = Gdx.app.getPreferences(YummyCarrot.FILENAME);
                scores.putInteger("lifetime", YummyCarrot.lifetime);
                if (score > YummyCarrot.best) {
                    YummyCarrot.best = score;
                    scores.putInteger("best", score);
                }
                scores.flush();
                //if (YummyCarrot.loses >= 5){
                    gsm.game.getHandler().showAds();
                    YummyCarrot.loses = 0;
                //}
                scores.putInteger("loses", YummyCarrot.loses);
                Gdx.audio.newMusic(Gdx.files.internal("lose.wav")).play();
                gsm.game.getHandler().showBanner(true);
                gsm.set(new MenuState(gsm));
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0, YummyCarrot.WIDTH, YummyCarrot.HEIGHT);
        sb.draw(ground.getTexture(), 0, 0, YummyCarrot.WIDTH, ground.getTexture().getHeight());
        for (Carrot egg : eggs) {
            sb.draw(egg.getTexture(), egg.getPosition().x, egg.getPosition().y);
        }
        sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        if (score % 50 == 0 && score > 0 && !isSound) {Gdx.audio.newMusic(Gdx.files.internal("ochko.wav")).play(); isSound = true;}
        else if (score % 50 != 0 ) isSound = false;
        font.draw(sb, Integer.toString(score), YummyCarrot.WIDTH / 2 , SCORE_HEIGHT-80, Align.center, Align.center, true);
        sb.draw(clouds, 0, YummyCarrot.HEIGHT-80, YummyCarrot.WIDTH, 80);
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
    }
}
