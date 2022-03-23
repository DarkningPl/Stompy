package com.qb.stompy;

import com.qb.stompy.dataReaders.ProgressReader;
import com.qb.stompy.scenes.MainMenuScene;
import com.qb.stompy.util.SaveDataException;
import com.rubynaxela.kyanite.game.Game;
import com.rubynaxela.kyanite.game.assets.*;
import com.rubynaxela.kyanite.util.data.JSONDataFile;
import com.rubynaxela.kyanite.window.Window;

import java.util.List;

public class StompyGame extends Game {

    public static ProgressReader progress = new JSONDataFile("savedata/worlds_data.json").convertTo(ProgressReader.class);

    public StompyGame() {
        final AssetsBundle assets = getContext().getAssetsBundle();
        final Window window = getContext().setupWindow(800, 600, "Stompy Game");
        final SaveDataException bonjour = new SaveDataException("Invalid lastCompetedLevel field in data file!");


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
        assets.register("texture_heart", new Texture("assets/images/simple_heart.png"));
        assets.register("texture_blurr", new Texture("assets/images/blurr.png"));

        assets.register("texture_map_point", new Texture("assets/images/map_point.png"));
        assets.register("texture_map_path", new TextureAtlas("assets/images/map_path.png"));

        assets.register("texture_strawberry", new Texture("assets/images/strawberry.png"));

        assets.register("font_mc", new FontFace("assets/fonts/Minecraftia-Regular.ttf"));

        assets.register("worlds", new DataAsset("savedata/worlds_data.json"));
        final ProgressReader.PRLastLevel lastLevel = assets.<DataAsset>get("worlds").convertTo(ProgressReader.class).getLastCompletedLevel();
        final List<ProgressReader.PRWorld> worlds = assets.<DataAsset>get("worlds").convertTo(ProgressReader.class).getWorlds();
        for (int i = 0; i < worlds.size(); i++) {
            if (worlds.get(i).getPaths().length != worlds.get(i).getBestScores().length + 1) throw new SaveDataException("Number of paths in world " + (i + 1) + " is incorrect relative to the number of levels in that world!");
            assets.register("world_" + i, new DataAsset("assets/data/world_" + i + "_data.json"));
        }
        if (lastLevel.worldNumber < 0 || lastLevel.worldNumber >= worlds.size()) throw new SaveDataException("Last world does not match a world in the game data!");
        else {
            if (lastLevel.levelNumber < 0 || lastLevel.levelNumber >= worlds.get(lastLevel.worldNumber).getPaths().length) throw new SaveDataException("Last level does not match a world in the game data!");
            else {
                for (int w = 0; w < lastLevel.worldNumber; w++) {
                    for (int l = 0; l < worlds.get(w).getPaths().length; l++) {
                        if (!worlds.get(w).getPaths()[l]) throw new SaveDataException("Not all previous paths are unlocked!");
                    }
                }
                for (int l = 0; l < lastLevel.levelNumber; l++) {
                    if (!worlds.get(lastLevel.worldNumber).getPaths()[l]) throw new SaveDataException("Not all previous paths are unlocked!");
                }
            }
        }
        assets.register("levels", new DataAsset("assets/data/levels_data.json"));
        //assets.register("character", new DataAsset("assets/data/character_data.json"));

        //DataFile dat = new DataFile("assets/data/test.txt");
        //dat.append("" + 32);
        //System.out.println(dat.read());

//        window.setScene(new LoadedWorldScene(0));

//        window.setScene(new LoadedWorldScene(lastLevel.worldNumber)); if (lastLevel.levelNumber >= 1) window.<LoadedWorldScene>getScene().placeCharacterAtLevel(lastLevel.levelNumber - 1);

//        window.setScene(new LoadedLevelScene(0, 0));
//        window.setScene(new StompyScene());

        window.setScene(new MainMenuScene());
    }
}
