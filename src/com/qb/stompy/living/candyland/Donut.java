package com.qb.stompy.living.candyland;

import com.qb.stompy.StompyScene;
import com.qb.stompy.living.RoundEnemy;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Drawable;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

public class Donut extends RoundEnemy {
    private float rSpeed = 0, frictionMultiplier;
    private boolean wasTossed = false;

    //Manual control
    public boolean leftPressed = false, rightPressed = false, upPressed = false, downPressed = false;

    public Donut(float diameter) {
        super(diameter);
        Texture texture = assets.get("texture_donut");
        texture.apply(this.mainBody);
        mainBody.setSize(Vec2.f(diameter, diameter));
        mainBody.setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y / 2);
        setMaxHp(1);
        frictionMultiplier = 0.01f;

        /*
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.key) {
                    case LEFT -> leftPressed = true;
                    case RIGHT -> rightPressed = true;
                    case DOWN -> downPressed = true;
                    case UP -> upPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.key) {
                    case LEFT -> leftPressed = false;
                    case RIGHT -> rightPressed = false;
                    case DOWN -> downPressed = false;
                    case UP -> upPressed = false;
                }
            }
        });

         */
    }

    public void toss(float strength, float angle) {
        float oneRotationDegree = angle;
        while (Math.abs(oneRotationDegree) > 180) {
            oneRotationDegree -= Math.signum(oneRotationDegree) * 360;
        }
        speedX = strength * (float) Math.cos(angle * Math.PI / 180);
        speedY = strength * (float) Math.sin(angle * Math.PI / 180);
        //rSpeed = 90 - Math.abs(oneRotationDegree);
        rSpeed = (float) (strength * Math.cos(angle * Math.PI / 180) / (2 * Math.PI * radius));
        System.out.println("Rotation speed: " + rSpeed);
    }

    public void adjustFriction(float multiplier) {
        frictionMultiplier = multiplier;
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        //Updating data
        if (currentHp <= 0) {
            kill();
        }
        speedY += 1000 * deltaTime.asSeconds();
        Vector2f mapOffset;


        //Manual control
        /*
        if (leftPressed && !rightPressed) {
            speedX = -120;
        } else if (!leftPressed && rightPressed) {
            speedX = 120;
        } else speedX = 0;
        if (upPressed && !downPressed) {
            speedY = -120;
        } else if (!upPressed && downPressed) {
            speedY = 120;
        } else speedY = 0;

         */

        //Loop variables
        float L = gGB().left + gGB().width / 2 - radius,
                R = gGB().left + gGB().width / 2 + radius,
                B = gGB().top + gGB().height / 2 + radius,
                T = gGB().top + gGB().height / 2 - radius;

        //Out of map prevention
        if (window.getScene() instanceof final StompyScene stompyScene) {
            if (-(L + speedX * deltaTime.asSeconds()) >= stompyScene.getMapOffset().x) {
                speedX = -L / deltaTime.asSeconds();
            }
            if (R + speedX * deltaTime.asSeconds() >= stompyScene.getMapSize().x) {
                speedX = (stompyScene.getMapSize().x - R) / deltaTime.asSeconds();
            }
        } else {
            if (-(L + speedX * deltaTime.asSeconds()) >= 0) {
                speedX = -L / deltaTime.asSeconds();
            }
            if (R + speedX * deltaTime.asSeconds() >= window.getSize().x) {
                speedX = (window.getSize().x - R) / deltaTime.asSeconds();
            }
        }

        if (Keyboard.isKeyPressed(Keyboard.Key.B)) {
            System.out.println("Left border: " + gGB().left + ", right border: " + (gGB().left + gGB().width) + ", middle: " + (gGB().left + gGB().width / 2) + ", position x on map: " + getPositionOnMap().x);
            System.out.println("Upper border: " + gGB().top + ", lower border: " + (gGB().top + gGB().height) + ", middle: " + (gGB().top + gGB().height / 2) + ", position y on map: " + getPositionOnMap().y);
            System.out.println("Radius : " + getRadius() + ", left edge: " + (getPositionOnMap().x - radius) + ", right edge: " + (getPositionOnMap().x + radius) + ", upper edge: " + (getPositionOnMap().y - radius) + ", lower edge: " + (getPositionOnMap().y + radius));
        }

        //Block collisions
//        for (final Drawable object : window.getScene()) {
//            if (object instanceof final SolidBlock sBlock) {
//                float L2 = sBlock.gGB().left, R2 = L2 + sBlock.gGB().width, T2 = sBlock.gGB().top, B2 = T2 + sBlock.gGB().height;
//                //Vertical Collision
//                if (L < R2 && R > L2) {
//                    if (T >= B2 && -speedY * deltaTime.asSeconds() >= T - B2) {
//                        speedY = (B2 - T) / deltaTime.asSeconds();
//                    }
//                    if (B <= T2 /* + 1 //TODO // test this sh!t */ && speedY * deltaTime.asSeconds() >= T2 - B) {
//                        speedY = (T2 - B) / deltaTime.asSeconds();
//                        onGround = true;
//                    }
//                }
//                //Horizontal Collision
//                if ((T < B2 && B > T2) || (T + speedY * deltaTime.asSeconds() < B2 && B + speedY * deltaTime.asSeconds() > T2)) {
//                    if (L >= R2 && -speedX * deltaTime.asSeconds() >= L - R2) {
//                        speedX = (R2 - L) / deltaTime.asSeconds();
//                    }
//                    if (R <= L2 && speedX * deltaTime.asSeconds() >= L2 - R) {
//                        speedX = (L2 - R) / deltaTime.asSeconds();
//                    }
//                }
//            }
//        }
        preventBlockCollision(deltaTime);

        //TOSS IT !!!
        if (!wasTossed && elapsedTime.asSeconds() > 4) {
            wasTossed = true;
            toss(700, -80);
            System.out.println("YEET!");
        }

        //setOrigin(Vec2.divide(Vec2.f(getGlobalBounds().width,getGlobalBounds().height),2));
        mainBody.rotate(rSpeed);

        moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());

    }
}
