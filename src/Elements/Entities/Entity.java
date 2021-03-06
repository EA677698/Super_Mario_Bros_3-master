package Elements.Entities;

import java.awt.*;
import Elements.Manager;
import Elements.Layer;
import Elements.Elements;
import Graphics.Window;
import Main.Global;
import Elements.Tiles.Tile;
import Main.Main;

public abstract class Entity extends Elements {

    private Point location;
    private Image baseSprite;
    private int width, height, life, damage,sprite,direction,touchingGround;
    private double XVelocity, YVelocity, gravity;
    private double gravityTimer = System.nanoTime();
    private Rectangle hitBox;
    private boolean allowedMoving = true;
    private boolean isDead = false;
    private boolean collision, offGround, isSelected, hasGravity;
    private String entityName;
    private final String localPath = Global.localPath;

    public Entity(Layer layer, Point coordinates, int width, int height, boolean hasCollision){
        super(layer, coordinates);
        location = new Point((int)(coordinates.x*Window.scaleX),(int)(coordinates.y*Window.scaleY));
        this.width = (int)(width*Window.scaleX);
        this.height = (int)(height*Window.scaleY);
        life = 1;
        collision = hasCollision;
        hasGravity = true;
        damage = 1;
        XVelocity = 1.0;
        gravity = 1;
        hitBox = new Rectangle(location.x, location.y,width,height);
    }

    public Entity(Layer layer, Point coordinates, int width, int height, int life, int damage, boolean hasGravity, boolean hasCollision){
        super(layer, coordinates);
        location = new Point((int)(coordinates.x*Window.scaleX),(int)(coordinates.y*Window.scaleY));
        this.width = (int)(width*Window.scaleX);
        this.height = (int)(height*Window.scaleY);
        this.life = life;
        this.damage = damage;
        this.hasGravity = hasGravity;
        XVelocity = 1.0;
        collision = hasCollision;
        gravity = 1;
        hitBox = new Rectangle(location.x, location.y,width,height);
    }

    public Entity(Layer layer, Point coordinates, int width, int height, int life, int damage, double XVelocity, double gravity, boolean hasGravity, boolean hasCollision){
        super(layer, coordinates);
        location = new Point((int)(coordinates.x*Window.scaleX),(int)(coordinates.y*Window.scaleY));
        this.width = (int)(width*Window.scaleX);
        this.height = (int)(height*Window.scaleY);
        this.life = life;
        this.damage = damage;
        collision = hasCollision;
        this.XVelocity = XVelocity;
        this.gravity = gravity;
        this.hasGravity = hasGravity;
        hitBox = new Rectangle(location.x, location.y,width,height);
    }

    public void tick(){
        touchingGround = 0;
        for(Tile tile: Main.game.getManager().getTiles()){
            if(tile.isCollision()){
                int side = tile.getHitBox().outcode(hitBox.getCenterX(),hitBox.getCenterY());
                if(hitBox.intersects(tile.getHitBox())&&(side==2||side==3||side==6)){
                    touchingGround++;
                }
            }
        }
        offGround = touchingGround <= 0;
        if(hasGravity){
            if(touchingGround==0&&!isSelected&&System.nanoTime()-gravityTimer>1200000){
                addY((int)(gravity));
                gravityTimer = System.nanoTime();
            }
        }
    }

    public void cullingException(){
        hitBox.setLocation(location.x, location.y);
        hitBox.setSize(width-20,height);
    }

    public boolean isFacingTile(Tile tile){
        if(tile == null){
            return false;
        }
        if(tile.getLocation().x<this.getLocation().x){
            if(this.getDirection()==-1){
                return true;
            }
        }
        if(tile.getLocation().x>this.getLocation().x){
            return this.getDirection() == 1;
        }
        return false;
    }

    public boolean isFacingEntity(Entity entity){
        if(entity==null){
            return false;
        }
        if(entity.getLocation().x<this.getLocation().x){
            if(this.getDirection()==-1){
                return true;
            }
        }
        if(entity.getLocation().x>this.getLocation().x){
            return this.getDirection() == 1;
        }
        return false;
    }

    public void setOffGround(boolean bool){
        offGround = bool;
    }

    public boolean isOffGround(){
        return offGround;
    }

    public int getTouchingGround(){
        return touchingGround;
    }

    public Rectangle getHitBox(){
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox){
        this.hitBox = hitBox;
    }

    public abstract void death();

    public abstract Image getSprite();

    public void addX(int x){
        location = new Point(location.x+x, location.y);
    }

    public void setX(int x){
        location = new Point(x,location.y);
    }

    public void setY(int y){
        location = new Point(location.x,y);
    }

    public void addY(int y){
        location = new Point(location.x, location.y+y);
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getEntityName(){
        return entityName;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getXVelocity() {
        return XVelocity;
    }

    public void setXVelocity(double XVelocity) {
        this.XVelocity = XVelocity;
    }

    public int getCurrentSprite() {
        return sprite;
    }

    public void setSprite(int sprite) {
        this.sprite = sprite;
    }


    public String toString(){
        return entityName+","+getLayer()+","+location.x+","+location.y+","+width+","+height+","+life+","+damage+
                ","+ XVelocity +","+gravity+","+hasGravity+","+collision+",";
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public boolean isAllowedMoving() {
        return allowedMoving;
    }

    public void setAllowedMoving(boolean allowedMoving) {
        this.allowedMoving = allowedMoving;
    }

    public Image getBaseSprite() {
        return baseSprite;
    }

    public void setBaseSprite(Image baseSprite) {
        this.baseSprite = baseSprite;
    }

    public Image[] getImages(){
        return null;
    }

    public double getYVelocity() {
        return YVelocity;
    }

    public void setYVelocity(double YVelocity) {
        this.YVelocity = YVelocity;
    }
}
