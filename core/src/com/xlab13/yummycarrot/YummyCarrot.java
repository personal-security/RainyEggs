package com.xlab13.yummycarrot;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xlab13.yummycarrot.states.GameStateManager;
import com.xlab13.yummycarrot.states.MenuState;

public class YummyCarrot extends ApplicationAdapter {

	private IActivityRequestHandler myRequestHandler;

	public static final int WIDTH = 432;
	public static final int HEIGHT = 720;
	public static final String TITLE = "Yummy Carrot";
	public static final String FILENAME = "DATA1";

	private GameStateManager gsm;
	private SpriteBatch batch;
	public static int best;
	public static int score;
	public static int lifetime;
	public static int loses;
	public static boolean isSound;
	public static Music music;

	public YummyCarrot(IActivityRequestHandler handler) {
		myRequestHandler = handler;
	}

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		gsm = new GameStateManager(this);
		Preferences scores = Gdx.app.getPreferences(FILENAME);
		isSound = scores.getBoolean("music", true);
		music = Gdx.audio.newMusic(Gdx.files.internal("ost.mp3"));
		music.setLooping(true);
		if (isSound){
			music.setVolume(0.1f);
		} else{
			music.setVolume(0f);
		}
		music.play();
		Gdx.gl.glClearColor(1, 1,1, 1);
		gsm.push(new MenuState(gsm));
		best = scores.getInteger("best", 0);
		lifetime = scores.getInteger("lifetime", 0);
		loses = scores.getInteger("loses", 0);
		score = 0;
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose () {
		super.dispose();
		music.dispose();
	}

	public IActivityRequestHandler getHandler() {
		return myRequestHandler;
	}
}