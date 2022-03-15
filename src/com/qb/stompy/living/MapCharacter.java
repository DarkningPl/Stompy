package com.qb.stompy.living;

import com.qb.stompy.HUDs.LevelLaunchHUD;
import com.qb.stompy.scenes.LoadedLevelScene;
import com.qb.stompy.scenes.LoadedWorldScene;
import com.qb.stompy.dataReaders.LevelReader;
import com.qb.stompy.dataReaders.ProgressReader;
import com.qb.stompy.objects.MapObject;
import com.qb.stompy.objects.MapPoint;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.event.KeyListener;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.KeyEvent;

import java.util.ArrayList;

public class MapCharacter extends MapObject implements AnimatedEntity {
    private int textureFace = 0;
    private final int animationFrames = 8;
    private float speedX, speedY, animationTime = 0;
    private final float movementSpeed = 120, animationLoopTime = 1.6f;
    private boolean leftPressed = false, rightPressed = false, upPressed = false, downPressed = false,
            sprinting = false, sprintPressed = false, sprintReleased = false, interaction = false;

    public MapCharacter() {
        Texture texture = assets.get("texture_strawberry");
        texture.apply(this);
        setTextureRect(new IntRect(0, 0, 32, 32));
        setSize(Vec2.f(32, 32));
        setOrigin(getSize().x / 2, getSize().y);

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.key) {
                    case A -> leftPressed = true;
                    case D -> rightPressed = true;
                    case S -> downPressed = true;
                    case W -> upPressed = true;
                    case LSHIFT -> sprintPressed = true;
                    case SPACE -> interaction = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.key) {
                    case A -> leftPressed = false;
                    case D -> rightPressed = false;
                    case S -> downPressed = false;
                    case W -> upPressed = false;
                    case LSHIFT -> sprintPressed = false;
                    case SPACE -> interaction = false;
                }
            }
        });
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        //Controls
        if (leftPressed && !rightPressed) {
            speedX = -movementSpeed;
            textureFace = 2;
        } else if (!leftPressed && rightPressed) {
            speedX = movementSpeed;
            textureFace = 3;
        } else speedX = 0;
        if (upPressed && !downPressed) {
            speedY = -movementSpeed;
            textureFace = 1;
        } else if (!upPressed && downPressed) {
            speedY = movementSpeed;
            textureFace = 0;
        } else speedY = 0;
        if (sprintPressed) {
            if (sprintReleased) {
                speedX = 2 * speedX;
                speedY = 2 * speedY;
            }
            sprinting = true;
        } else {
            if (sprinting) {
                speedX *= 0.5;
                speedY *= 0.5;
            }
            sprinting = false;
            sprintReleased = true;
        }

        //Loop Variables
        int frame = 0;
        if (speedY == 0) frame += (int)(animationFrames * animationTime / animationLoopTime);
        float X = gGB().left + getOrigin().x, Y = gGB().top + getOrigin().y, movementX = speedX, movementY = speedY;

        //Updating data
        animationTime += deltaTime.asSeconds();
        if (animationTime >= animationLoopTime) animationTime -= animationLoopTime;
        if (speedX == 0 && (textureFace == 2 || textureFace == 3)) frame = 0;
        if (speedY == 0 && (textureFace == 0 || textureFace == 1)) frame = 0;

        //Preventing out of path
        if (getWorldScene() != null) {
            boolean hasBeenOnPathForward = false, hasBeenOnPathBackward = false,
                    slowLF = false, slowLB = false,
                    slowRF = false, slowRB = false,
                    slowUF = false, slowUB = false,
                    slowDF = false, slowDB = false;
            float distLF = 0, distLB = 0, distRF = 0, distRB = 0, distUF = 0, distUB = 0, distDF = 0, distDB = 0;
            ProgressReader.PRWorld world = assets.<DataAsset>get("maps").convertTo(ProgressReader.class).getWorlds().get(getWorldScene().getWorldNumber());
            for (int i = 0; i < getWorldScene().getPaths().size(); i++) {
                float L = getWorldScene().getPaths().get(i).gGB().left, R = L + getWorldScene().getPaths().get(i).gGB().width,
                        T = getWorldScene().getPaths().get(i).gGB().top, B = T + getWorldScene().getPaths().get(i).gGB().height;
                boolean isOnPath = X >= L && X <= R && Y >= T && Y <= B;
                boolean isUnlocked = world.isPathUnlocked(getWorldScene().getPaths().get(i).getLevelNumber());
                if (hasBeenOnPathForward && isOnPath && isUnlocked) {
                    movementX = speedX;
                    movementY = speedY;
                    slowDF = false;
                    slowLF = false;
                    slowRF = false;
                    slowUF = false;
                }
                if (isOnPath)
                    hasBeenOnPathForward = true;
                if (isOnPath) {
                    if (speedX > 0) {
                        if (speedX * deltaTime.asSeconds() >= R - X) {
                            slowRF = true;
                            distRF = (R - X) / deltaTime.asSeconds();
                        }
                    } else if (speedX < 0) {
                        if (-speedX * deltaTime.asSeconds() >= X - L) {
                            slowLF = true;
                            distLF = (L - X) / deltaTime.asSeconds();
                        }
                    }
                    if (speedY > 0) {
                        if (speedY * deltaTime.asSeconds() >= B - Y) {
                            slowDF = true;
                            distDF = (B - Y) / deltaTime.asSeconds();
                        }
                    } else if (speedY < 0) {
                        if (-speedY * deltaTime.asSeconds() >= Y - T) {
                            slowUF = true;
                            distUF = (T - Y) / deltaTime.asSeconds();
                        }
                    }
                }
            }
            for (int i = getWorldScene().getPaths().size(); i > 0; i--) {
                float L = getWorldScene().getPaths().get(i - 1).gGB().left, R = L + getWorldScene().getPaths().get(i - 1).gGB().width,
                        T = getWorldScene().getPaths().get(i - 1).gGB().top, B = T + getWorldScene().getPaths().get(i - 1).gGB().height;
                boolean isOnPath = X >= L && X <= R && Y >= T && Y <= B;
                boolean isUnlocked = world.isPathUnlocked(getWorldScene().getPaths().get(i - 1).getLevelNumber());//.paths.get(getWorldScene().getPaths().get(i).getLevelNumber()).unlocked;
                if (hasBeenOnPathBackward && isOnPath && isUnlocked) {
                    movementX = speedX;
                    movementY = speedY;
                    slowDB = false;
                    slowLB = false;
                    slowRB = false;
                    slowUB = false;
                }
                if (isOnPath)
                    hasBeenOnPathBackward = true;
                if (isOnPath) {
                    if (speedX > 0) {
                        if (speedX * deltaTime.asSeconds() >= R - X) {
                            slowRB = true;
                            distRB = (R - X) / deltaTime.asSeconds();
                        }
                    } else if (speedX < 0) {
                        if (-speedX * deltaTime.asSeconds() >= X - L) {
                            slowLB = true;
                            distLB = (L - X) / deltaTime.asSeconds();
                        }
                    }
                    if (speedY > 0) {
                        if (speedY * deltaTime.asSeconds() >= B - Y) {
                            slowDB = true;
                            distDB = (B - Y) / deltaTime.asSeconds();
                        }
                    } else if (speedY < 0) {
                        if (-speedY * deltaTime.asSeconds() >= Y - T) {
                            slowUB = true;
                            distUB = (T - Y) / deltaTime.asSeconds();
                        }
                    }
                }
            }
            if (slowRB && slowRF)
                movementX = Math.max(distRB, distRF);
            if (slowLB && slowLF)
                movementX = Math.max(distLB, distLF);
            if (slowDB && slowDF)
                movementY = Math.max(distDB, distDF);
            if (slowUB && slowUF)
                movementY = Math.max(distUB, distUF);
            ArrayList<MapPoint> points = getWorldScene().getLevelPoints();
            Vector2f offset = getWorldScene().getMapOffset();
            for (int i = 0; i < points.size(); i++) {
                if (interaction) {
                    if (points.get(i).gGB().contains(Vec2.subtract(getPositionOnMap(), offset))) {
                        //Previous world
                        if (i == 0) {
                            if (getWorldScene().getWorldNumber() != 0) {
                                window.setScene(new LoadedWorldScene(getWorldScene().getWorldNumber() - 1));
                                window.<LoadedWorldScene>getScene().placeCharacterAtEnd();
                            }
                        }
                        //Next world
                        else if (i == points.size() - 1) {
                            if (i != assets.<DataAsset>get("levels").convertTo(LevelReader.class).getWorlds().size() - 1) {
                                window.setScene(new LoadedWorldScene(getWorldScene().getWorldNumber() + 1));
                            } else {
                                //play a final cutscene or something
                            }
                        }
                        //Any level
                        else {
                            //TODO place an HUD
//                            window.setHUD(new LevelLaunchHUD(getWorldScene().getWorldNumber(), i - 1));
                            window.setScene(new LoadedLevelScene(getWorldScene().getWorldNumber(), i - 1));
                        }
                    }
                }
            }
        }

        setTextureRect(new IntRect(frame * 32, textureFace * 32, 32, 32));

        moveOnMap(movementX * deltaTime.asSeconds(), movementY * deltaTime.asSeconds());
    }
}
