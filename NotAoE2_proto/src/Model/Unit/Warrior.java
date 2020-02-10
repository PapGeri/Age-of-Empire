package Model.Unit;

public class Warrior extends Entity{
    
   public Warrior(int posX, int posY, int unitIndex){
       this.posX = posX;
       this.posY = posY;
       this.unitIndex = unitIndex;
       
       this.type = 4;
       this.health = 50;
       this.power = 10;
       this.defence = 0;
       
       this.wheatCost = 80;
       this.goldCost = 0;
       this.actionPointUsage = 3;
       this.popCount = 1;
       this.range = 1;
   }
   
}
