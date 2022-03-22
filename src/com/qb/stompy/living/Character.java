package com.qb.stompy.living;

import com.qb.stompy.living.candyland.Cookie;
import com.qb.stompy.scenes.LoadedWorldScene;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.event.KeyListener;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;
import org.jsfml.window.event.KeyEvent;

public class Character extends LivingGameObject implements AnimatedEntity {
    private int frame = 0, idleFrame = 0, idleFrame2 = 0;
    private final int animationFrames = 8;
    private float speedX = 0, speedY = 0, timeSinceBoing = 0, jumpPressedTime = 0, animationTime = 0, idleTime = 0, idleActionTime = 0;
    private final float movementSpeed = 120, jumpStrength = 540, boingStrength = jumpStrength / (float) Math.sqrt(2), animationLoopTime = 1.6f, idleTimeTillAction = 5;
    private boolean sprinting = false, sprintReleased = false, boinged = false, isIdle = false, idleAction = false;
    private boolean leftPressed = false, rightPressed = false, jumpPressed = false, sprintPressed = false;
    private boolean[] idleActions;

    public Character() {
        Texture texture = assets.get("texture_strawberry");
        texture.apply(this.mainBody);
        mainBody.setTextureRect(new IntRect(0, 0, 32, 32));
        mainBody.setSize(Vec2.f(32, 32));
        mainBody.setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y);
        setMaxHp(3);
        invincibilityTime = 1;
        idleActions = new boolean[]{false, false, false};

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.key) {
                    case LEFT -> leftPressed = true;
                    case RIGHT -> rightPressed = true;
                    case LSHIFT -> sprintPressed = true;
                    case SPACE -> jumpPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.key) {
                    case LEFT -> leftPressed = false;
                    case RIGHT -> rightPressed = false;
                    case LSHIFT -> sprintPressed = false;
                    case SPACE -> jumpPressed = false;
                }
            }
        });
    }

    private void unlockPath(int worldNumber, int pathNumber) {
        //overwrite value in progress data at said numbers
    }

    private void steerCharacter(Time deltaTime) {
        if (leftPressed || rightPressed) {
            isIdle = false;
            idleTime = 0;
            idleAction = false;
            idleActionTime = 0;
            idleActions = new boolean[]{false, false, false};
        }
        if (leftPressed && rightPressed) {
            speedX = 0;
            sprintReleased = false;
        } else if (leftPressed) {
            speedX = -movementSpeed;
            frame = 2;
        } else if (rightPressed) {
            speedX = movementSpeed;
            frame = 3;
        } else speedX = 0;
        if (jumpPressed) {
            jumpPressedTime += deltaTime.asSeconds();
            if ((onGround || boinged) && jumpPressedTime < 0.1) {
                if (onGround) onGround = false;
                speedY = -jumpStrength;
            }
            isIdle = false;
            idleTime = 0;
            idleAction = false;
            idleActionTime = 0;
            idleActions = new boolean[]{false, false, false};
        } else jumpPressedTime = 0;
        if (sprintPressed) {
            if (sprinting || onGround) {
                if (sprintReleased)
                    speedX = 2 * speedX;
                sprinting = true;
            }
        } else {
            if (sprinting) speedX *= 0.5;
            sprinting = false;
            sprintReleased = true;
        }
    }

    private void animateIdle() {
        if (idleActions[0]) { //Blink
            if (idleActionTime > 0.5)
                idleFrame = 1;
            else if (idleActionTime > 0.4)
                idleFrame = 2;
            else if (idleActionTime > 0.3)
                idleFrame = 3;
            else if (idleActionTime > 0.2)
                idleFrame = 2;
            else if (idleActionTime > 0.1)
                idleFrame = 1;
            if (idleActionTime <= 0) {
                idleAction = false;
                idleTime = 2 * (float)Math.random();
                idleActions[0] = false;
            }
        }
        else if (idleActions[1]) { //Look around
            if (idleActionTime > 2.4) {
                idleFrame = 1;
                idleFrame2 = 1;
            }
            if (idleActionTime > 0 && idleActionTime < 1.2) {
                idleFrame = 2;
                idleFrame2 = 1;
            }
            if (idleActionTime <= 0) {
                idleAction = false;
                idleTime = 2 * (float)Math.random();
                idleActions[1] = false;
            }
        }
        else if (idleActions[2]) { //Yawn
            if (idleActionTime > 4.8)
                idleFrame = 4;
            else if (idleActionTime > 4.6)
                idleFrame = 5;
            else if (idleActionTime > 4.4)
                idleFrame = 6;
            else if (idleActionTime > 0.8)
                idleFrame = 7;
            else if (idleActionTime > 0.6)
                idleFrame = 6;
            else if (idleActionTime > 0.4)
                idleFrame = 5;
            else if (idleActionTime > 0.2)
                idleFrame = 4;
            if (idleActionTime <= 0) {
                idleAction = false;
                idleTime = 2 * (float)Math.random();
                idleActions[2] = false;
            }
        }
    }

    public void bounce() {
        speedY = -boingStrength;
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (!getLevelScene().isPaused()) {
            frame = 0;
            idleFrame = 0;
            idleFrame2 = 0;
            isIdle = true;

            //Controls
            steerCharacter(deltaTime);

            //Updating data
            if (currentHp <= 0) kill();
            if (affectedByGravity) speedY += 1000 * deltaTime.asSeconds();
            if (boinged) {
                timeSinceBoing += deltaTime.asSeconds();
                if (timeSinceBoing >= 0.1) {
                    boinged = false;
                    timeSinceBoing = 0;
                }
            }
            if (recentlyDamaged) {
                invincibilityTime -= deltaTime.asSeconds();
                if (invincibilityTime < 0) invincibilityTime = 0;
                mainBody.setFillColor(new Color(255, 255, 255, (int) (255 - 127 * Math.sin(invincibilityTime * Math.PI))));
                if (invincibilityTime == 0) {
                    recentlyDamaged = false;
                    canBeDamaged = true;
                    invincibilityTime = 1;
                }
            }
            animationTime += deltaTime.asSeconds();
            if (animationTime >= animationLoopTime) animationTime -= animationLoopTime;
            if (isIdle && !idleAction) idleTime += deltaTime.asSeconds();
            idleAction = idleTime >= idleTimeTillAction;
            if (idleAction) idleActionTime -= deltaTime.asSeconds();
            if (idleAction && idleActionTime < 0 && !(idleActions[0] || idleActions[1] || idleActions[2])) {
                int randNumb = (int) (15 * Math.random());
                System.out.println(randNumb);
                if (randNumb < 10) {
                    idleActionTime = 0.5f;
                    idleActions[0] = true;
                } else if (randNumb < 14) {
                    idleActionTime = 3.6f;
                    idleActions[1] = true;
                } else {
                    idleActionTime = 5;
                    idleActions[2] = true;
                }
            }

            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            animateIdle();
            mainBody.setTextureRect(new IntRect(32 * idleFrame, 32 * idleFrame2, 32, 32));

            //Loop Variables
            float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;
            int animationFrame = (int) (animationFrames * animationTime / animationLoopTime);
            if (speedX == 0) animationFrame = 0;

            //Out of map prevention
            if (-(L + speedX * deltaTime.asSeconds()) >= 0) {
                speedX = -L / deltaTime.asSeconds();
            }
            if (R + speedX * deltaTime.asSeconds() >= window.getSize().x) {
                speedX = (window.getSize().x - R) / deltaTime.asSeconds();
            }

            //Checking collisions
            for (final Drawable object : window.getScene()) {
                //Enemies collision
                if (object instanceof final Enemy en) {
                    if (en instanceof final RoundEnemy ren) {
                    /*float X = ren.gGB().left + ren.gGB().width / 2, L2 = X - ren.getRadius(), R2 = X + ren.getRadius(),
                            Y = ren.gGB().top + ren.gGB().height / 2, B2 = Y + ren.getRadius(), T2 = Y - ren.getRadius();
                    FloatRect intersection = gGB().intersection(ren.gGB());
                    if (L <= X && R >= X) {
                        if (ren.canBeStomped() && ren.canBeDamaged) {
                            if (B < T2 && speedY * deltaTime.asSeconds() >= T2 - B || B == T2) {
                                speedY = -boingStrength;
                                ren.stomp();
                            }
                        }
                    }
                    else if (L > ren.getPositionOnMap().x && L < L2) {
                        if (ren.canBeStomped() && ren.canBeDamaged) {
                            if (B < Y && speedY > 0) {
                                if (B + speedY * deltaTime.asSeconds() - Y >= Math.sqrt(Math.pow(ren.getRadius(), 2) - Math.pow(L + speedX * deltaTime.asSeconds() - X, 2))) {
                                    speedY = -boingStrength;
                                    ren.stomp();
                                }
                            }
                        }
                    }
                    else if (R < ren.getPositionOnMap().x && R > R2) {
                        if (ren.canBeStomped() && ren.canBeDamaged) {
                            if (B < Y && speedY > 0) {
                                if (B + speedY * deltaTime.asSeconds() - Y >= Math.sqrt(Math.pow(ren.getRadius(), 2) - Math.pow(R + speedX * deltaTime.asSeconds() - X, 2))) {
                                    speedY = -boingStrength;
                                    ren.stomp();
                                }
                            }
                        }
                    }
                    if (intersection != null) {
                        if (!((Math.pow(R - X, 2) + Math.pow(B - Y, 2) >= Math.pow(ren.getRadius(), 2)) && (Math.pow(L - X, 2) + Math.pow(B - Y, 2) >= Math.pow(ren.getRadius(), 2)) &&
                                (Math.pow(R - X, 2) + Math.pow(T - Y, 2) >= Math.pow(ren.getRadius(), 2)) && (Math.pow(L - X, 2) + Math.pow(T - Y, 2) >= Math.pow(ren.getRadius(), 2)))) {
                            damage(1);
                        }
                    }*/
                        float L2 = ren.getSquareHitbox().getPosition().x, R2 = L2 + ren.getSquareHitbox().getSize().x, T2 = ren.getSquareHitbox().getPosition().y;
                        if (L < R2 && R > L2) {
                            if (ren.canBeStomped() && ren.canBeDamaged) {
                                if (B < T2 && speedY * deltaTime.asSeconds() >= T2 - B || B == T2) {
                                    speedY = -boingStrength;
                                    ren.stomp();
                                }
                            }
                        }
                        if (this.intersects(ren) && ren.isActive()) {
                            damage(1);
                        }
                    } else {
                        float L2 = en.gGB().left, R2 = L2 + en.gGB().width, T2 = en.gGB().top;
                        if (L < R2 && R > L2) {
                            if (en.canBeStomped() && en.canBeDamaged) {
                                if (B == T2 || (B < T2 && ((speedY - en.getSpeed().y) * deltaTime.asSeconds() >= T2 - B || speedY * deltaTime.asSeconds() >= T2 - B))) {
                                    bounce();
                                    if (en instanceof Cookie) boinged = true;
                                    en.stomp();
                                }
                            }
                        }
                        if (this.intersects(en) && en.isActive()) {
                            damage(1);
                        }
                    }
                }
                //Terrain collision
                if (object instanceof final SolidBlock sBlock) {
                    float L2 = sBlock.gGB().left, R2 = L2 + sBlock.gGB().width, T2 = sBlock.gGB().top, B2 = T2 + sBlock.gGB().height;
                    //Vertical Collision
                    if (L < R2 && R > L2) {
                        if (T >= B2 && -speedY * deltaTime.asSeconds() >= T - B2) {
                            speedY = (B2 - T) / deltaTime.asSeconds();
                        }
                        if (B <= T2 && speedY * deltaTime.asSeconds() >= T2 - B) {
                            speedY = (T2 - B) / deltaTime.asSeconds();
                            onGround = true;
                        }
                    }
                    //Horizontal Collision
                    if ((T < B2 && B > T2) || (T + speedY * deltaTime.asSeconds() < B2 && B + speedY * deltaTime.asSeconds() > T2)) {
                        if (L >= R2 && -speedX * deltaTime.asSeconds() >= L - R2) {
                            speedX = (R2 - L) / deltaTime.asSeconds();
                        }
                        if (R <= L2 && speedX * deltaTime.asSeconds() >= L2 - R) {
                            speedX = (L2 - R) / deltaTime.asSeconds();
                        }
                    }
                }
            }

            //Last updates
            if (speedY > 0) onGround = false;
            if (!onGround) animationFrame = 0;
            if (!idleAction) mainBody.setTextureRect(new IntRect(32 * animationFrame, 32 * frame, 32, 32));

            if (/*character got to a certain point on map*/getPositionOnMap().x >= getLevelScene().getMapSize().x - 200) {
                //TODO update conditions to match new readers
//            //unlock the next path in the same world
//            unlockPath(getLevelScene().getWorldNumber(), getLevelScene().getLevelNumber() + 1);
//            if (getLevelScene().getLevelNumber() == assets.<DataAsset>get("levels").convertTo(LevelReader.class).getWorlds().get(getLevelScene().getWorldNumber()).getLevels().size() - 1) {
//                //play a world's end cutscene maybe
//                if (getLevelScene().getWorldNumber() != assets.<DataAsset>get("levels").convertTo(LevelReader.class).getWorlds().size() - 1) {
//                    //unlock the first path in next world
//
//                }
//            }
                //return to the world
                int targetLevel = getLevelScene().getLevelNumber();
                window.setScene(new LoadedWorldScene(getLevelScene().getWorldNumber()));
                window.<LoadedWorldScene>getScene().placeCharacterAtLevel(targetLevel);
            }

            moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());
        }
    }
}
