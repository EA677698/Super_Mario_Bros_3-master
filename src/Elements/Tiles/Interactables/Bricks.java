package Elements.Tiles.Interactables;

import Elements.Entities.Entity;
import Elements.Manager;
import Elements.Entities.Mario.Powers;
import Elements.Layer;
import Main.Main;
import java.awt.*;

public class Bricks extends Interactable {

    private Image[] blocks;
    private Entity containedEntity;
    private boolean breakable;
    private double executeTimer = System.nanoTime();
    private double timer = System.nanoTime();

    public Bricks(Layer layer, Point location, boolean collision, Entity containedEntity) {
        super(layer, location, collision);
        this.containedEntity = containedEntity;
        setTileName("Brick");
        this.breakable = false;
    }

    public Bricks(Layer layer, Point location, boolean collision, boolean breakable) {
        super(layer, location, collision);
        setTileName("Brick");
        this.breakable = breakable;
    }

    @Override
    public void tick() {
        super.tick();
        if(!isActivated()){
            if(Main.game.getManager().getPlayer()!=null) {
                if (Main.game.getManager().getPlayer().getHitBox().intersects(getHitBox())&&getHitBox().outcode(Main.game.getManager().getPlayer().getHitBox().getCenterX(),Main.game.getManager().getPlayer().getHitBox().getCenterY())==8) {
                    executeOnTouch();
                }
            }
            if(System.nanoTime()-timer>200000000){
                setCurrentSprite(getCurrentSprite()+1);
                if(getCurrentSprite()>3){
                    setCurrentSprite(0);
                }
                timer = System.nanoTime();
            }
        } else {
            setCurrentSprite(4);
        }
    }

    @Override
    public void executeOnTouch() {
        setActivated(true);
        if(breakable&&Main.game.getManager().getPlayer().getPower()== Powers.BIG){
            removeFromLayer();
            Main.game.getManager().getTiles().remove(this);
        }
        if(containedEntity != null){
            containedEntity.setLocation(new Point(getLocation().x,getLocation().y));
            Main.game.getManager().getEnts().add(containedEntity);
            if(System.nanoTime()-executeTimer>100000000){
                containedEntity.addY(-1);
                executeTimer = System.nanoTime();
            }
        }
        setCurrentSprite(4);
    }

    @Override
    public void executeOnAction() {

    }

    @Override
    public Image[] getSprites() {
        return Main.game.getSpritesLoader().getBricks();
    }

    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public Entity getContainedEntity() {
        return containedEntity;
    }

    public void setContainedEntity(Entity containedEntity) {
        this.containedEntity = containedEntity;
    }

    @Override
    public String toString() {
        return super.toString()+breakable+",";
    }
}
