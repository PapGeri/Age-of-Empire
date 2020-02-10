package Model.Building;

public class Tavern extends Buildings{
  public Tavern(int PosX, int PosY, int buildingIndex) {
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 7;
         this.health = 100;
         this.score = 0;
         this.woodCost = 50;
         this.goldCost = 100;
  }  
}
