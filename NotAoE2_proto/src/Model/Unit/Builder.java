package Model.Unit;

public class Builder extends Entity{
   public Builder (int posX, int posY, int unitIndex) {
       this.posX = posX;
       this.posY = posY;
       this.unitIndex = unitIndex;
       
       this.type = 0;
       this.health = 60;
       this.power = 2;
       this.defence = 0;
       
       this.wheatCost = 120;
       this.goldCost = 20;
       this.actionPointUsage = 4;
       this.popCount = 5;
       this.range = 1;
    }  
}
