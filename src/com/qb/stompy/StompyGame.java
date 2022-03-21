package com.qb.stompy;

import com.qb.stompy.dataReaders.ProgressReader;
import com.qb.stompy.scenes.LoadedWorldScene;
import com.rubynaxela.kyanite.game.Game;
import com.rubynaxela.kyanite.game.assets.*;
import com.rubynaxela.kyanite.window.Window;

import java.util.List;

public class StompyGame extends Game {
    public StompyGame(){
        final AssetsBundle assets = getContext().getAssetsBundle();
        final Window window = getContext().setupWindow(800, 600, "Stompy Game");


        assets.register("texture_candy", new TextureAtlas("assets/images/candyland/candy.png"));
        assets.register("texture_caramel", new Texture("assets/images/candyland/caramel.png"));
        assets.register("texture_chocolate", new Texture("assets/images/candyland/chocolate.png"));
        assets.register("texture_cookie", new Texture("assets/images/candyland/cookie.png"));
        assets.register("texture_cotton_candy", new Texture("assets/images/candyland/cotton_candy.png"));
        assets.register("texture_cupcake", new TextureAtlas("assets/images/candyland/cupcake.png"));
        assets.register("texture_donut", new Texture("assets/images/candyland/donut.png"));

        assets.register("texture_sky", new Texture("assets/images/sky.png"));
        assets.register("texture_dirt", new Texture("assets/images/dirt.png"));
        assets.register("texture_grassy_dirt", new Texture("assets/images/grassy_dirt.png"));
        assets.register("texture_brick", new Texture("assets/images/brick.png"));
        assets.register("texture_meadow", new Texture("assets/images/meadow.png"));
        assets.register("texture_blocks", new TextureAtlas("assets/images/blocks.png"));

        assets.register("texture_map_point", new Texture("assets/images/map_point.png"));
        assets.register("texture_map_path", new TextureAtlas("assets/images/map_path.png"));

        assets.register("texture_strawberry", new Texture("assets/images/strawberry.png"));

        assets.register("font_mc", new FontFace("assets/fonts/Minecraftia-Regular.ttf"));

        assets.register("worlds", new DataAsset("assets/data/worlds_data.json"));
        final ProgressReader.PRLastLevel lastLevel = assets.<DataAsset>get("worlds").convertTo(ProgressReader.class).getLastCompletedLevel();
        final List<ProgressReader.PRWorld> worlds = assets.<DataAsset>get("worlds").convertTo(ProgressReader.class).getWorlds();
        for (int i = 0; i < worlds.size(); i++) {
            //TODO Jacek help
//            try {
//                if (worlds.get(i).getPaths().length != worlds.get(i).getBestScores().length + 1) {
//                    bruh
//                }
//                else{
//                    if (lastLevel.worldNumber < 0 || lastLevel.worldNumber >= worlds.size()) { also bruh }
//                    else {
//                        if (lastLevel.levelNumber < 0 || lastLevel.levelNumber >= worlds.get(lastLevel.worldNumber).getPaths().length) { bruh too }
//                    }
//                }
//            } catch(a bruh) {
//
//            }
            assets.register("world_" + i, new DataAsset("assets/data/world_" + i + "_data.json"));
        }
        assets.register("levels", new DataAsset("assets/data/levels_data.json"));
        //assets.register("character", new DataAsset("assets/data/character_data.json"));

        //DataFile dat = new DataFile("assets/data/test.txt");
        //dat.append("" + 32);
        //System.out.println(dat.read());

        window.setScene(new LoadedWorldScene(0));

        window.setScene(new LoadedWorldScene(lastLevel.worldNumber));
        if (lastLevel.levelNumber >= 1) window.<LoadedWorldScene>getScene().placeCharacterAtLevel(lastLevel.levelNumber - 1);

//        window.setScene(new LoadedLevelScene(0, 2));
//        window.setScene(new StompyScene());
    }
}
