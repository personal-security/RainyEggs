package com.xlab13.yummycarrot.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xlab13.yummycarrot.YummyCarrot;

import java.util.Stack;

/**
 * Created by obitola on 12/22/2017.
 */

public class GameStateManager {

    public YummyCarrot game;
    private Stack<State> states;

    public GameStateManager(YummyCarrot game){
        this.game = game;
        states = new Stack<State>();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }
}