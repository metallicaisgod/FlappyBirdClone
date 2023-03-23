package com.kirillm.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;

	Texture[] bird;
	int birdStateFlag = 0;
	float flyHeight;
	float fallingSpeed = 0;
	int gameStateFlag = 0;

	Texture topTube;
	Texture bottomTube;
	int spaceBetweenTubes = 500;

	Random random;
	int tubeSpeed = 5;
	float[] tubeX = new float[5];
	float[] tubeShift = new float[5];
	float distanceBetweenTubes;
	int tubesNumber = 5;

	Circle birdCircle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
//	ShapeRenderer shapeRenderer;

	int gameScore = 0;
	int passTubeIndex = 0;
	BitmapFont bitmapFont;

	Texture  gameOver;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		birdCircle = new Circle();
//		shapeRenderer = new ShapeRenderer();

		topTubeRectangles = new Rectangle[tubesNumber];
		bottomTubeRectangles = new Rectangle[tubesNumber];

		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");
		topTube = new Texture("top_tube.png");
		bottomTube = new Texture("bottom_tube.png");
		random = new Random();

		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.CYAN);
		bitmapFont.getData().setScale(10);

		gameOver = new Texture("game_over.png");


		startPosition();
	}

	private void startPosition(){
		gameScore = 0;
		passTubeIndex = 0;
		fallingSpeed = 0;
		flyHeight = Gdx.graphics.getHeight() / 2 - bird[birdStateFlag].getHeight() / 2;
		distanceBetweenTubes = Gdx.graphics.getWidth() / 2;
		for(int i = 0; i < tubesNumber; i ++) {
			tubeX[i] = Gdx.graphics.getWidth() + i * distanceBetweenTubes  * 1.5f;
			tubeShift[i] = (random.nextFloat() - 0.5f) *
					(Gdx.graphics.getHeight() -
							spaceBetweenTubes - 200);
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
//		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if(gameStateFlag == 1) {

			if(tubeX[passTubeIndex] < Gdx.graphics.getWidth() / 2 - topTube.getWidth()){
				gameScore++;
				if(passTubeIndex < tubesNumber -1){
					passTubeIndex++;
				} else {
					passTubeIndex = 0;
				}
			}

			if(Gdx.input.justTouched()){
				fallingSpeed = -15;
			}
			if(flyHeight > 0 || fallingSpeed < 0) {
				fallingSpeed += 0.5;
				flyHeight -= fallingSpeed;
			}

			for(int i = 0; i < tubesNumber; i ++) {

				if(tubeX[i] < -topTube.getWidth()){
					tubeX[i] = tubesNumber * distanceBetweenTubes * 1.5f;
					tubeShift[i] = (random.nextFloat() - 0.5f) *
							(Gdx.graphics.getHeight() -
									spaceBetweenTubes - 200);
				} else {
					tubeX[i] -=tubeSpeed;
				}
				batch.draw(topTube, tubeX[i],
						(float) (Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2) + tubeShift[i]);

				batch.draw(bottomTube, tubeX[i],
						(float) (Gdx.graphics.getHeight() / 2 -
								spaceBetweenTubes / 2 - bottomTube.getHeight()) + tubeShift[i]);

				topTubeRectangles[i].set(tubeX[i],
						(float) (Gdx.graphics.getHeight() / 2 +
								spaceBetweenTubes / 2) + tubeShift[i],
						topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i].set(tubeX[i],
						(float) (Gdx.graphics.getHeight() / 2 -
								spaceBetweenTubes / 2 - bottomTube.getHeight()) + tubeShift[i],
						bottomTube.getWidth(), bottomTube.getHeight());
			}

		} else if(gameStateFlag == 0) {
			if(Gdx.input.justTouched()){
				gameStateFlag = 1;
			}
		} else if(gameStateFlag == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
			if(Gdx.input.justTouched()){
				startPosition();
				gameStateFlag = 1;
			}
		}

		if(birdStateFlag == 0){
			birdStateFlag = 1;
		} else {
			birdStateFlag = 0;
		}

		batch.draw(bird[birdStateFlag],
				Gdx.graphics.getWidth() / 2 - bird[birdStateFlag].getWidth() / 2,
				flyHeight);
		bitmapFont.draw(batch, String.valueOf(gameScore), 100, 200);

		batch.end();

		birdCircle.set((float)Gdx.graphics.getWidth() / 2,
				flyHeight + (float)bird[birdStateFlag].getHeight() / 2,
				(float)bird[birdStateFlag].getWidth() / 2);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.CYAN);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
//		shapeRenderer.setColor(Color.CORAL.r, Color.CORAL.g, Color.CORAL.b, 0.1f);
		for(int i = 0; i < tubesNumber && gameStateFlag == 1; i ++){
//			shapeRenderer.rect(topTubeRectangles[i].x, topTubeRectangles[i].y,
//					topTubeRectangles[i].width, topTubeRectangles[i].height);
//			shapeRenderer.rect(bottomTubeRectangles[i].x, bottomTubeRectangles[i].y,
//					bottomTubeRectangles[i].width, bottomTubeRectangles[i].height);
			if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) ||
					Intersector.overlaps(birdCircle, bottomTubeRectangles[i]) ){
				gameStateFlag = 2;
				break;
			}
		}

//		shapeRenderer.end();

	}
}
