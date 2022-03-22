package com.qb.stompy.buttons;

import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.game.entities.CompoundEntity;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import com.rubynaxela.kyanite.window.event.MouseButtonListener;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.MouseButtonEvent;

public abstract class GameButton extends CompoundEntity implements AnimatedEntity {

    private final RectangleShape interior;
    private boolean isLPressed = false, wasLPressed = false, cancelPress = false, selected = false, shouldUnselect = false;
    protected Window window = GameContext.getInstance().getWindow();
    private final Vector2f size;

    public GameButton(Vector2f buttonSize, String message) {
        this(buttonSize, 8, message, Colors.BLACK);
    }

    public GameButton(Vector2f buttonSize, int fontSize, String message, Color textColor) {
        size = buttonSize;
        Color black = Colors.BLACK;

        Text msg = new Text(message, new Font(GameContext.getInstance().getAssetsBundle().get("font_mc"), fontSize));
        RectangleShape bBorder = new RectangleShape(Vec2.f(buttonSize.x, 1));
        RectangleShape lBorder = new RectangleShape(Vec2.f(1, buttonSize.y));
        RectangleShape rBorder = new RectangleShape(Vec2.f(1, buttonSize.y));
        RectangleShape tBorder = new RectangleShape(Vec2.f(buttonSize.x, 1));
        interior = new RectangleShape(Vec2.f(buttonSize.x - 2, buttonSize.y - 2));
        bBorder.setPosition(-0.5f * buttonSize.x, 0.5f * buttonSize.y - 1);
        lBorder.setPosition(Vec2.divideFloat(buttonSize, -2));
        rBorder.setPosition(0.5f * buttonSize.x - 1, -0.5f * buttonSize.y);
        tBorder.setPosition(Vec2.divideFloat(buttonSize, -2));
        interior.setPosition(-0.5f * buttonSize.x + 1, -0.5f * buttonSize.y + 1);
        bBorder.setFillColor(black);
        lBorder.setFillColor(black);
        rBorder.setFillColor(black);
        tBorder.setFillColor(black);
//        interior.setFillColor(new Color(116, 98, 37));
        msg.setColor(textColor);
        msg.setAlignment(Text.Alignment.TOP_CENTER);

        add(bBorder);
        add(lBorder);
        add(rBorder);
        add(tBorder);
        add(interior);
        add(msg);

        GameContext.getInstance().getWindow().addMouseButtonListener(new MouseButtonListener() {
            @Override
            public void mouseButtonPressed(MouseButtonEvent e) {
                if (e.button == Mouse.Button.LEFT) {
                    isLPressed = true;
                }
            }

            @Override
            public void mouseButtonReleased(MouseButtonEvent e) {
                if (e.button == Mouse.Button.LEFT) {
                    isLPressed = false;
                    cancelPress = false;
                }
            }
        });
    }

    public abstract void clickAction();

    public void select() {
        selected = true;
    }
    public void unselect() {
        selected = false;
    }
    public boolean isSelected() {
        return selected;
    }
    public Vector2f getSize() { return size; }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        Color idleColor = new Color(116, 98, 37);
        Color hoverColor = new Color(151, 128, 50);
        if (getGlobalBounds().contains(Vec2.f(Mouse.getPosition(GameContext.getInstance().getWindow())))) {
            interior.setFillColor(hoverColor);
            if (selected && Keyboard.isKeyPressed(Keyboard.Key.SPACE)) clickAction();
            shouldUnselect = true;
        } else {
            interior.setFillColor(idleColor);
            if (isLPressed) cancelPress = true;
            if (shouldUnselect) {
                unselect();
                shouldUnselect = false;
            }
            if (selected) {
                interior.setFillColor(hoverColor);
                if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) clickAction();
            } else interior.setFillColor(idleColor);
        }

        //Do action
        if (!isLPressed && wasLPressed) {
            clickAction();
        }

        wasLPressed = isLPressed && !cancelPress;
    }
}
