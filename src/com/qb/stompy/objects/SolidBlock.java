package com.qb.stompy.objects;

import org.jsfml.system.Vector2f;

public class SolidBlock extends GameObject {
    public SolidBlock(Vector2f size){
        super();
        mainBody.setSize(size);
    }
}
