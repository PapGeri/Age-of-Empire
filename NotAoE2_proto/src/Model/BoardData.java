package Model;

import java.awt.Image;

public class BoardData {
   private int zoomLevel = 0;
   private Image [] images;
   private Image[][] imagesArray;
    public BoardData(){
        
    }

    public void setImages(Image[] images) {
        this.images = images;
        Image images8[]  = new Image[images.length];
        Image images10[] = new Image[images.length];
        Image images16[] = new Image[images.length];
        Image images20[] = new Image[images.length];
        imagesArray = new Image[4][images.length];
        for(int i = 0; i < images.length;++i){
            images8[i] = images[i];
            images10[i] = images[i].getScaledInstance(80, 80, 
                    java.awt.Image.SCALE_SMOOTH);
            images16[i] = images[i].getScaledInstance(52, 51, 
                    java.awt.Image.SCALE_SMOOTH);
            images20[i] = images[i].getScaledInstance(43, 42, 
                    java.awt.Image.SCALE_SMOOTH);
        }
        imagesArray[0] = images8;
        imagesArray[1] = images10;
        imagesArray[2] = images16;
        imagesArray[3] = images20;
    }

    public Image getImages(int i) {
        return images[i];
    }
    
    public void zoomImages(boolean b){
        if (b){
            zoomLevel++;
        } else {
            zoomLevel--;
        }
        images = imagesArray[zoomLevel];
    }
}
