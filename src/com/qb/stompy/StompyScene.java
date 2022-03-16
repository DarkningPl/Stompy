package com.qb.stompy;

import com.qb.stompy.living.*;
import com.qb.stompy.living.Character;
import com.qb.stompy.living.candyland.*;
import com.qb.stompy.objects.GameObject;
import com.qb.stompy.objects.MapObject;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

public class StompyScene extends Scene {
    private int indexToRemove = -1;
    private final Vector2f mapSize;
    private Vector2f mapOffset = Vec2.f(0, 0);
    private boolean display = true, shouldRemoveObject = false;


    //
    public StompyScene() {
        mapSize = Vec2.f(4000, 600);
        //
    }

    public Vector2f getMapSize() {
        return mapSize;
    }

    public Vector2f getMapOffset() {
        return mapOffset;
    }

    public void moveMap(Vector2f offset) {
        mapOffset = Vec2.add(mapOffset, offset);
    }

    public void moveMap(float x, float y) {
        mapOffset = Vec2.add(mapOffset, Vec2.f(x, y));
    }

    CircleShape nutRoundHitbox = new CircleShape();
    RectangleShape nutRectActBox = new RectangleShape();

    public Character getPlayerCharacter() {
        for (final Drawable obj : getContext().getWindow().getScene()) {
            if (obj instanceof Character)
                return (Character) obj;
        }
        return null;
    }

    @Override
    protected void init() {
        //
        MapObject background = new MapObject();
        Texture sky = getContext().getAssetsBundle().get("texture_sky");
        sky.apply(background.mainBody);
        background.mainBody.setSize(mapSize);
        add(background);

        //
        SolidBlock floor = new SolidBlock(Vec2.f(mapSize.x, 20));
        floor.setPositionOnMap(0, getContext().getWindow().getSize().y - floor.mainBody.getSize().y);
        floor.mainBody.setFillColor(Colors.SANDY_BROWN);
        add(floor);

        for (int i = 0; i < 10; i++) {
            SolidBlock block = new SolidBlock(Vec2.f(40, 40));
            block.setPositionOnMap(60 + 200 * i, 80 + 10 * i);
            block.mainBody.setFillColor(Colors.SANDY_BROWN);
            //add(block);
        }


//        Character character = new Character();
//        character.setPositionOnMap(60, 460);
//        add(character);


        //Blob testEn = new Blob();
        //testEn.setPositionOnMap(getContext().getWindow().getSize().x / 2.0f, getContext().getWindow().getSize().y - floor.getSize().y);
        //add(testEn);

        Candy testCandy = new Candy(Colors.LIME);
        testCandy.setPositionOnMap(470, 300);
        //add(testCandy);
        Candy testCandy2 = new Candy(Colors.YELLOW);
        testCandy2.setPositionOnMap(210, 500);
        //add(testCandy2);
        Candy testCandy3 = new Candy(Colors.RED);
        testCandy3.setPositionOnMap(570, 100);
        //add(testCandy3);
        Candy testCandy4 = new Candy(Colors.BLUE);
        testCandy4.setPositionOnMap(310, 380);
        //add(testCandy4);

        Cookie testPizza = new Cookie();
        testPizza.setPositionOnMap(0, 540);
        add(testPizza);

        Cupcake testCupcake = new Cupcake();
        testCupcake.setPositionOnMap(700, 200);
//        add(testCupcake);

        Chocolate testChoco = new Chocolate(5);
        testChoco.setPositionOnMap(700, 200);
//        add(testChoco);

        Donut testDonut = new Donut(32);
        testDonut.setPositionOnMap(400, 200);
        add(testDonut);

        SolidBlock cPlatform = new SolidBlock(Vec2.f(800, 20));
        cPlatform.setPositionOnMap(500, 480);
        cPlatform.mainBody.setFillColor(Colors.SANDY_BROWN);
        add(cPlatform);

        //Blurr experiment
        RectangleShape darkness = new RectangleShape(Vec2.f(getContext().getWindow().getSize().x, getContext().getWindow().getSize().y));
        darkness.setFillColor(Colors.opacity(Colors.BLACK, 222 / 255f));
        //add(darkness);

        CircleShape halo = new CircleShape();
        halo.setRadius(80);
        halo.setPosition(200, 500);
        halo.setFillColor(Color.TRANSPARENT);
        //add(halo);

/*
        getContext().getWindow().setKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.key) {
                    case A -> character.leftPressed = true;
                    case D -> character.rightPressed = true;
                    case S -> character.downPressed = true;
                    case W -> character.upPressed = true;
                    case LEFT -> testDonut.leftPressed = true;
                    case RIGHT -> testDonut.rightPressed = true;
                    case DOWN -> testDonut.downPressed = true;
                    case UP -> testDonut.upPressed = true;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.key) {
                    case A -> character.leftPressed = false;
                    case D -> character.rightPressed = false;
                    case S -> character.downPressed = false;
                    case W -> character.upPressed = false;
                    case LEFT -> testDonut.leftPressed = false;
                    case RIGHT -> testDonut.rightPressed = false;
                    case DOWN -> testDonut.downPressed = false;
                    case UP -> testDonut.upPressed = false;
                }

            }
        });

 */

        nutRoundHitbox.setPosition(testDonut.getPositionOnMap());
        nutRoundHitbox.setRadius(testDonut.getRadius());
        nutRoundHitbox.setOrigin(testDonut.getRadius(), testDonut.getRadius());
        nutRoundHitbox.setFillColor(new Color(0, 0, 255, 128));
        //add(nutRoundHitbox);
        nutRectActBox.setSize(testDonut.getSquareHitbox().getSize());
        nutRectActBox.setPosition(testDonut.getSquareHitbox().getPosition());
        nutRectActBox.setFillColor(new Color(0, 255, 0, 128));
        //add(nutRectActBox);

//
//        GameButton button = new GameButton("butt", Vec2.i(64, 32));
//        button.setPosition(Vec2.divide(Vec2.f(getContext().getWindow().getSize()), 2));
//        add(button);
    }

    @Override
    protected void loop() {
        float offsetX = 0, offsetY = 0;
        //
        if (Keyboard.isKeyPressed(Keyboard.Key.L)) {
            if (display) {
                int i = 0;
                int i2 = 0;
                System.out.println("Map offset: " + mapOffset.x + ", " + mapOffset.y);
                for (final Drawable obj : getContext().getWindow().getScene()) {
                    if (obj instanceof GameObject) {
                        if (!(obj instanceof SolidBlock)) {
                            i++;
                            System.out.println("Object " + i + " bounds: " + ((GameObject) obj).gGB().left + ", " + ((GameObject) obj).gGB().top + ", " +
                                    (((GameObject) obj).gGB().left + ((GameObject) obj).gGB().width) + ", " + (((GameObject) obj).gGB().top + ((GameObject) obj).gGB().height));
                        } else {
                            i2++;
                            System.out.println("Block " + i2 + " bounds: " + ((GameObject) obj).gGB().left + ", " + ((GameObject) obj).gGB().top + ", " +
                                    (((GameObject) obj).gGB().left + ((GameObject) obj).gGB().width) + ", " + (((GameObject) obj).gGB().top + ((GameObject) obj).gGB().height));
                        }
                    }
                }
                display = false;
            }
        } else display = true;

        if (getPlayerCharacter() != null) {
            if (getPlayerCharacter().getPositionOnMap().x > getContext().getWindow().getSize().x / 2.0f) {
                if (getPlayerCharacter().getPositionOnMap().x < getMapSize().x - getContext().getWindow().getSize().x / 2.0f)
                    offsetX = getPlayerCharacter().getPositionOnMap().x - getContext().getWindow().getSize().x / 2.0f;
                else
                    offsetX = getMapSize().x - getContext().getWindow().getSize().x;
            }
            mapOffset = Vec2.f(offsetX, offsetY);
        }

        for (final Drawable obj : getContext().getWindow().getScene()) {
            if (obj instanceof final GameObject gameObject) {
                gameObject.setPosition(Vec2.subtract(gameObject.getPositionOnMap(), mapOffset));
                if (!shouldRemoveObject) {
                    if (gameObject.gGB().top > getMapSize().y || gameObject.gGB().left > getMapSize().x || gameObject.gGB().left + gameObject.gGB().width < 0 - mapOffset.x) {
                        indexToRemove = getContext().getWindow().getScene().indexOf(obj);
                        shouldRemoveObject = true;
                    }
                    if (gameObject instanceof final LivingGameObject livingGameObject) {
                        if (livingGameObject.isDead()) {
                            indexToRemove = getContext().getWindow().getScene().indexOf(obj);
                            shouldRemoveObject = true;
                        }
                    }
                }
            }
        }
        if (shouldRemoveObject) {
            remove(indexToRemove);
            indexToRemove = -1;
            shouldRemoveObject = false;
        }
    }
}
