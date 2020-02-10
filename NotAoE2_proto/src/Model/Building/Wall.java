package Model.Building;

public class Wall extends Buildings{
    public Wall(int PosX, int PosY, int buildingIndex) {
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 8;
         this.health = 50;
         this.score = 0;
         this.woodCost = 30;
         this.goldCost = 0;
    }
}
