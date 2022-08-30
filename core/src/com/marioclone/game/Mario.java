package com.marioclone.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Mario extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] redHairedBoy;
	int boyIdxRun = 3;
	int pauseRun = 0;
	float gravity = 0.98f;
	float velocity = 0f;
	int redHairedBoyY = 0;
	//END
	int gamestate = 0;
	//Score + Collision
	int maxScore = 0;
	int score = 0;
	BitmapFont font;
	BitmapFont font2;
	BitmapFont waiting;
	BitmapFont gameOver;
	Rectangle boyRectangle;
	//COIN
	ArrayList<Integer> coinXs = new ArrayList<>();
	ArrayList<Integer> coinYs = new ArrayList<>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<>();
	Texture coin;
	int coincount = 0;
	//ROCK
	ArrayList<Integer> rockYs = new ArrayList<>();
	ArrayList<Integer> rockXs = new ArrayList<>();
	ArrayList<Rectangle> rockRectangles = new ArrayList<>();
	Texture rock;
	int rockcount = 0;
	Random random;
	int idx = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background_mario.png");
		redHairedBoy = new Texture[10];
		redHairedBoy[0] = new Texture("idle (1).png");
		redHairedBoy[1] = new Texture("idle (2).png");
		redHairedBoy[2] = new Texture("hit.png");
		redHairedBoy[3] = new Texture("run (1).png");
		redHairedBoy[4] = new Texture("run (2).png");
		redHairedBoy[5] = new Texture("run (3).png");
		redHairedBoy[6] = new Texture("run (4).png");
		redHairedBoy[7] = new Texture("run (5).png");
		redHairedBoy[8] = new Texture("run (6).png");
		redHairedBoy[9] = new Texture("jump (1).png");

		redHairedBoyY = Gdx.graphics.getHeight() / 8 + 42;

		coin = new Texture("coinR.png");
		rock = new Texture("rock.png");
		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.DARK_GRAY);
		font.getData().setScale(6);

		font2 = new BitmapFont();
		font2.setColor(Color.GOLD);
		font2.getData().setScale(12);

		waiting = new BitmapFont();
		waiting.setColor(Color.GREEN);
		waiting.getData().setScale(7);

		gameOver = new BitmapFont();
		gameOver.setColor(Color.RED);
		gameOver.getData().setScale(6);
	}

	public void makeCoin() {
		float height = random.nextFloat() * (Gdx.graphics.getHeight());
		if (height < Gdx.graphics.getHeight() / 8 + 42) {
			height = Gdx.graphics.getHeight() / 8 + 200;
		}
		if (height > Gdx.graphics.getHeight() / 8 + 650) {
			height = Gdx.graphics.getHeight() / 8 + 50 + random.nextInt(600);
		}
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeRock() {
		float height = random.nextFloat() * (Gdx.graphics.getHeight());
		if (height < Gdx.graphics.getHeight() / 8 + 42) {
			height = Gdx.graphics.getHeight() / 8 + 200;
		}
		if (height > Gdx.graphics.getHeight() / 8 + 650) {
			height = Gdx.graphics.getHeight() / 8 + 50 + random.nextInt(600);
		}
		rockYs.add((int)height);
		rockXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gamestate == 1) {
			//GAME IS LIVE
			//Rock
			if(rockcount < 500) {
				rockcount = rockcount + 1;
			}else {
				rockcount = 0;
				makeRock();
			}
			rockRectangles.clear();
			for (int i = 0; i < rockXs.size(); i++) {
				batch.draw(rock,rockXs.get(i),rockYs.get(i));
				rockXs.set(i,rockXs.get(i) - 12);
				rockRectangles.add(new Rectangle(rockXs.get(i),rockYs.get(i),rock.getWidth(),rock.getHeight()));
			}

			//Coins
			if(coincount < 200) {
				coincount++;
			}else {
				coincount = 0;
				makeCoin();
			}
			coinRectangles.clear();
			for (int i = 0; i < coinXs.size(); i++) {
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i) - 6);
				coinRectangles.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}

			if (Gdx.input.justTouched()) {
				if (redHairedBoyY <= Gdx.graphics.getHeight() / 8 + 42) {
					velocity = -30f;
				}
			}
			if (pauseRun < 4) {
				pauseRun++;
			}else {
				pauseRun = 0;
				if (boyIdxRun > 2 && boyIdxRun < 8) {
					boyIdxRun++;
				} else {
					boyIdxRun = 3;
				}
			}
			velocity += gravity;
			redHairedBoyY -= velocity;
			if (redHairedBoyY <= Gdx.graphics.getHeight() / 8 + 42) {
				redHairedBoyY = Gdx.graphics.getHeight() / 8 + 42;
			}
			if (redHairedBoyY ==  Gdx.graphics.getHeight() / 8 + 42) {
				batch.draw(redHairedBoy[boyIdxRun], Gdx.graphics.getWidth() / 16 - 18, redHairedBoyY);
			}else if (redHairedBoyY > Gdx.graphics.getHeight() / 8 + 42) {
				batch.draw(redHairedBoy[9], Gdx.graphics.getWidth() / 16 - 18, redHairedBoyY);
			}
		}else if (gamestate == 0) {
			//WAITING TO START
			waiting.draw(batch,"Touch to Play",Gdx.graphics.getWidth() / 4,Gdx.graphics.getHeight() / 2);
			batch.draw(redHairedBoy[idx], Gdx.graphics.getWidth() / 16 - 18, redHairedBoyY);
			if (pauseRun < 10) {
				pauseRun++;
			}else {
				pauseRun = 0;
				idx += 1;
				idx %= 2;
			}

			if (Gdx.input.justTouched()) {
				gamestate = 1;
			}
		}else if (gamestate == 2) {
			batch.draw(redHairedBoy[9], Gdx.graphics.getWidth() / 16 - 18, redHairedBoyY);
			gameOver.draw(batch,"      Game Over \nTouch to Play Again",Gdx.graphics.getWidth() / 6,Gdx.graphics.getHeight() / 2);
			if (Gdx.input.justTouched()) {
				gamestate = 1;
				redHairedBoyY = Gdx.graphics.getHeight() / 8 + 42;
				if (score >= maxScore) {
					maxScore = score;
				}
				score = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coincount = 0;
				pauseRun = 0;
				rockXs.clear();
				rockYs.clear();
				rockRectangles.clear();
				rockcount = 0;
			}
		}
		//COLLISION
		boyRectangle = new Rectangle(Gdx.graphics.getWidth() / 16 - 18,redHairedBoyY,redHairedBoy[boyIdxRun].getWidth(),redHairedBoy[boyIdxRun].getHeight());
		for (int i = 0; i < coinRectangles.size(); i++) {
			if (Intersector.overlaps(boyRectangle,coinRectangles.get(i))) {
				score++;
				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}
		for (int i = 0; i < rockRectangles.size(); i++) {
			if (Intersector.overlaps(boyRectangle,rockRectangles.get(i))) {
				Gdx.app.log("Rock","Collision");
				rockRectangles.remove(i);
				rockXs.remove(i);
				rockYs.remove(i);
				gamestate = 2;
				break;
			}
		}
		font.draw(batch,"Max Score:" + maxScore,570,Gdx.graphics.getHeight() - 100);
		font2.draw(batch, String.valueOf(score),100,200);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
