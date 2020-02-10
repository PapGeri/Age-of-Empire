package Model.Building;

public class Barracks extends Buildings{
     public Barracks(int PosX, int PosY, int buildingIndex){
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 3;
         this.health = 250;
         this.score = 5;
         this.woodCost = 200;
         this.goldCost = 0;
     }
}
