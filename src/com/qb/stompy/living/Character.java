package com.qb.stompy.living;

import com.qb.stompy.loaders.LoadedWorldScene;
import com.qb.stompy.loaders.ProgressReader;
import com.qb.stompy.objects.Enemy;
import com.qb.stompy.objects.MapPath;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.event.KeyListener;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;
import org.jsfml.window.event.KeyEvent;

public class Character extends LivingGameObject implements AnimatedEntity {
    private final int animationFrames = 8;
    private float speedX = 0, speedY = 0, timeSinceBoing = 0, jumpPressedTime = 0, animationTime = 0;
    private final float movementSpeed = 120, jumpStrength = 500, boingStrength = jumpStrength / (float) Math.sqrt(2), animationLoopTime = 1.6f;
    private boolean sprinting = false, sprintReleased = false, boinged = false,
            sprintPressed = false, jumpPressed = false;
    public boolean leftPressed = false, rightPressed = false;

    public Character() {
        Texture texture = assets.get("texture_strawberry");
        texture.apply(this);
        setTextureRect(new IntRect(0, 0, 32, 32));
        setSize(Vec2.f(32, 32));
        setOrigin(getSize().x / 2, getSize().y);
        setMaxHp(3);
        invincibilityTime = 1;
//*
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.key) {
                    case A -> leftPressed = true;
                    case D -> rightPressed = true;
                    case LSHIFT -> sprintPressed = true;
                    case SPACE -> jumpPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.key) {
                    case A -> leftPressed = false;
                    case D -> rightPressed = false;
                    case LSHIFT -> sprintPressed = false;
                    case SPACE -> jumpPressed = false;
                }
            }
        });

        //*/
    }

    private void unlockPath(int worldNumber, int pathNumber) {
        //overwrite value in progress data at said numbers
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        int frame = 0;

        //Controls
        if (leftPressed && rightPressed) {
            speedX = 0;
            sprintReleased = false;
        } else if (leftPressed) {
            speedX = -movementSpeed;
            frame = 1;
        } else if (rightPressed) {
            speedX = movementSpeed;
            frame = 2;
        } else speedX = 0;
        if (jumpPressed) {
            jumpPressedTime += deltaTime.asSeconds();
            if ((onGround || boinged) && jumpPressedTime < 0.1) {
                if (onGround) onGround = false;
                speedY = -jumpStrength;
            }
        } else jumpPressedTime = 0;
        if (sprintPressed) {
            if (sprintReleased)
                speedX = 2 * speedX;
            sprinting = true;
        } else {
            if (sprinting) speedX *= 0.5;
            sprinting = false;
            sprintReleased = true;
        }

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
            setFillColor(new Color(255, 255, 255, (int) (255 - 127 * Math.sin(invincibilityTime * Math.PI))));
            if (invincibilityTime == 0) {
                recentlyDamaged = false;
                canBeDamaged = true;
                invincibilityTime = 1;
            }
        }
        animationTime += deltaTime.asSeconds();
        if (animationTime >= animationLoopTime) animationTime -= animationLoopTime;

        //Loop Variables
        float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;
        int animationFrame = (int)(animationFrames * animationTime / animationLoopTime);
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
                    FloatRect intersection = gGB().intersection(ren.getSquareHitbox().getGlobalBounds());
                    if (L < R2 && R > L2) {
                        if (ren.canBeStomped() && ren.canBeDamaged) {
                            if (B < T2 && speedY * deltaTime.asSeconds() >= T2 - B || B == T2) {
                                speedY = -boingStrength;
                                ren.stomp();
                            }
                        }
                    }
                    if (intersection != null) {
                        damage(1);
                    }
                } else {
                    float L2 = en.gGB().left, R2 = L2 + en.gGB().width, T2 = en.gGB().top;
                    FloatRect intersection = gGB().intersection(en.gGB());
                    if (L < R2 && R > L2) {
                        if (en.canBeStomped() && en.canBeDamaged) {
                            if (B < T2 && speedY * deltaTime.asSeconds() >= T2 - B || B == T2) {
                                speedY = -boingStrength;
                                if (en instanceof Pizza) boinged = true;
                                en.stomp();
                            }
                        }
                    }
                    if (intersection != null) {
                        damage(1);
                    }
                }
            }
        }
        if (speedY > 0) onGround = false;
        setTextureRect(new IntRect(32 * animationFrame, 32 * frame, 32, 32));

        if(/*character got to a certain point on map*/getPositionOnMap().x >= getLevelScene().getMapSize().x - 200) {
            unlockPath(getLevelScene().getWorldNumber(), getLevelScene().getLevelNumber() + 1);
            //unlock the first path from next world
            if (/*getLevelScene().getLevelNumber() == getWorldScene().getLevelPoints().size() - 2 CHANGE getWorldScene, it's null*/false) {
                if (/*world is not the last one*/true) {
                    //unlock first path in next world

                } else ; //play a cutscene or something, idk
            }
            //return to the world
            int targetLevel = getLevelScene().getLevelNumber();
            window.setScene(new LoadedWorldScene(getLevelScene().getWorldNumber()));
            window.<LoadedWorldScene>getScene().placeCharacterAtLevel(targetLevel);
        }
        moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());
    }
}
