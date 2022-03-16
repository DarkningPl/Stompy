package com.qb.stompy.objects;

import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Vec2;

public class MapPoint extends MapObject {
    public MapPoint(float scale) {
        super();
        Texture texture = assets.get("texture_map_point");
        texture.apply(this.mainBody);
        mainBody.setSize(Vec2.f(texture.getSize().x * scale, texture.getSize().y * scale));
        setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y / 2);
    }
}
