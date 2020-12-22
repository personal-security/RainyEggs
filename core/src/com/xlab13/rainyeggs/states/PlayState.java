package com.xlab13.rainyeggs.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.xlab13.rainyeggs.RainyEggsGame;
import com.xlab13.rainyeggs.sprites.Egg;
import com.xlab13.rainyeggs.sprites.Ground;
import com.xlab13.rainyeggs.sprites.Player;


/**
 * Created by obitola on 12/24/2017.
 */

public class PlayState extends State {

    private static final int START_HEIGHT = RainyEggsGame.HEIGHT;
    private static final int SCORE_HEIGHT = 700;

//    private Texture background;
    private float scaleX;
    private float scaleY;
    private BitmapFont font;
    private Vector2 position;
    private int score;
    private Array<Egg> eggs;
    private Ground ground;
    private Player player;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        eggs = new Array<Egg>();
        eggs.add(new Egg(START_HEIGHT));
        for (int i = 1; i < eggs.get(0).EGG_COUNT; i++){
            eggs.add(new Egg(START_HEIGHT + (i * eggs.get(0).GAP)));
        }

        ground = new Ground();
        player = new Player(RainyEggsGame.WIDTH / 2, ground.getTexture().getHeight());
        cam.setToOrtho(false, RainyEggsGame.WIDTH, RainyEggsGame.HEIGHT);
        scaleX = cam.viewportWidth / Gdx.graphics.getWidth();
        scaleY = cam.viewportHeight / Gdx.graphics.getHeight();
        font = new BitmapFont(Gdx.files.internal("hehe.fnt"));
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
            Egg egg = eggs.get(i);
            egg.update(dt);
            if (egg.isTouching(player.getHitBox())) {
                egg.reposition();
                score++;
            } else if (egg.getPosition().y <= ground.getTexture().getHeight()) {
                RainyEggsGame.score = score;
                RainyEggsGame.lifetime = RainyEggsGame.lifetime + score;
                Preferences scores = Gdx.app.getPreferences(RainyEggsGame.FILENAME);
                scores.putInteger("lifetime", RainyEggsGame.lifetime);
                if (score > RainyEggsGame.best) {
                    RainyEggsGame.best = score;
                    scores.putInteger("best", score);
                }
                scores.flush();
                gsm.set(new MenuState(gsm));
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
//        sb.draw(background, 0, 0, FallingEggs.WIDTH, FallingEggs.HEIGHT);
        sb.draw(ground.getTexture(), 0, 0, RainyEggsGame.WIDTH, ground.getTexture().getHeight());
        for (Egg egg : eggs) {
            sb.draw(egg.getTexture(), egg.getPosition().x, egg.getPosition().y);
        }
        sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        font.draw(sb, Integer.toString(score),RainyEggsGame.WIDTH / 2 , SCORE_HEIGHT, Align.center, Align.center, true);
        sb.end();
    }

    @Override
    public void dispose() {
//        background.dispose();
        font.dispose();
        for (Egg egg : eggs){
            egg.dispose();
        }
        ground.dispose();
        player.dispose();
    }
}
