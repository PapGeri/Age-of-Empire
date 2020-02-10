package Model;

public class Deployed {
    
    private int posX;
    private int posY;
    private int actualPlayer;
    private int unitIndex;
    private int type; //0 entity, 1 building

    public Deployed (int x, int y, int aP, int uI, int type) {
        this.posX = x;
        this.posY = y; 
        this.actualPlayer = aP;
        this.unitIndex = uI;
        this.type = type;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(int actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public int getUnitIndex() {
        return unitIndex;
    }

    public void setUnitIndex(int unitIndex) {
        this.unitIndex = unitIndex;
    }
   
    
}
