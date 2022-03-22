package com.qb.stompy.buttons;

import com.rubynaxela.kyanite.game.GameContext;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class ExitPromptButton extends GameButton {
    public ExitPromptButton(Vector2f buttonSize, String message) {
        super(buttonSize, message);
    }
    public ExitPromptButton(Vector2f buttonSize, int fontSize, String message, Color textColor) {
        super(buttonSize, fontSize, message, textColor);
    }

    @Override
    public void clickAction() {
        window.close();
    }
}
