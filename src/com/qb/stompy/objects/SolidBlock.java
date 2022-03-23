package com.qb.stompy.objects;

import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import org.jsfml.system.Vector2f;

public class SolidBlock extends GameObject {
    private TextureAtlas atlas = assets.get("texture_blocks");
    private Texture[][] textures = atlas.getMatrix(32, 32, 4, 1);
    public SolidBlock(Vector2f size){
        super();
        mainBody.setSize(size);
    }
    public void setTextureTile(String textureName) {
        switch (textureName) {
            case "dirt" -> {
                textures[0][0].setTileable(true);
                textures[0][0].apply(this.mainBody);
            }
            case "grassy_dirt" -> {
                textures[1][0].setTileable(true);
                textures[1][0].apply(this.mainBody);
            }
            case "stone_brick" -> {
                textures[2][0].setTileable(true);
                textures[2][0].apply(this.mainBody);
            }
            case "stone" -> {
                textures[3][0].setTileable(true);
                textures[3][0].apply(this.mainBody);
            }
        }
    }
}
