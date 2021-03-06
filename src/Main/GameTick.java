package Main;

import Elements.Entities.Entity;
import Elements.Manager;
import Graphics.SpritesLoader;
import Graphics.Window;
import Level.*;
import Settings.Controls;
import Settings.Settings;
import Sound.BGM;
import Sound.BGMPlayer;
import Elements.Tiles.Tile;
import Sound.SFX;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class GameTick implements Runnable {

    private Thread thread;
    public static int tps = 0;
    public static int count = 0;
    double timer = System.nanoTime();
    public static boolean clipReset;
    static SFX sfx;

    BGMPlayer bgmPlayer;

    private final SpritesLoader spritesLoader = new SpritesLoader();
    Window window;
    Manager manager;

    private boolean running;

    public GameTick(){
        try {
            manager = new Manager();
            sfx = new SFX();
            bgmPlayer = new BGMPlayer(BGM.GRASS_LAND);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        manager.setLevel(new Level(Background.AQUA_BACKGROUND, BGM.GRASS_LAND));
        window = new Window();
        bgmPlayer.getMusic().start();
        while(running){
            count++;
            Controls.tick();
            Settings.tick();
            manager.tick();
            Global.HUDcheck();

            for(Entity ent:manager.getEnts()){
                if(!ent.isUnloaded()){
                    ent.tick();
                }
                ent.cullingException();
            }
            for(Tile tile:manager.getTiles()){
                if(!tile.isUnloaded()){
                    tile.tick();
                }
                tile.callingException();
            }
            Window.screen.repaint();
            if(System.nanoTime()-timer>1000000000){
                tps = count;
                count = 0;
                timer = System.nanoTime();
            }
        }
        stop();
    }

    public synchronized void start(){
        if(running){return;}
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop(){
        if(!running){return;}
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void muteBGM(){
        if(!Settings.muted&&!clipReset){
            if(bgmPlayer.getMusic()!=null) {
                bgmPlayer.getMusic().setFramePosition(0);
                bgmPlayer.getMusic().stop();
            }
            bgmPlayer.getMusic().start();
        } else if(Settings.muted){
            clipReset = false;
        }
    }

    public SpritesLoader getSpritesLoader() {
        return spritesLoader;
    }

    public BGMPlayer getBgmPlayer() {
        return bgmPlayer;
    }

    public Manager getManager() {
        return manager;
    }

}
