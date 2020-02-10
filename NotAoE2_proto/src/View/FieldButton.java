package View;

import javax.swing.JButton;

public class FieldButton extends JButton{
    private int x,y;
    private int imgID;
    private boolean hasUnit;
    private int indexUnitNumber;
    
    
    public FieldButton(int x, int y, int imgID, boolean hasUnit){
        this.x = x;
        this.y = y;
        this.imgID = imgID;
        this.hasUnit = hasUnit;
    }
    
    public FieldButton(int x, int y, int imgID, boolean hasUnit, int iUN ){
        this.x = x;
        this.y = y;
        this.imgID = imgID;
        this.hasUnit = hasUnit;
        this.indexUnitNumber = iUN;
    }

    public int getXb() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getYb() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }    

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }
    
    public boolean isHasUnit() {
        return hasUnit;
    }

    public void setHasUnit(boolean hasUnit) {
        this.hasUnit = hasUnit;
    }

    public int getIndexUnitNumber() {
        return indexUnitNumber;
    }

    public void setIndexUnitNumber(int indexUnitNumber) {
        this.indexUnitNumber = indexUnitNumber;
    }
}
