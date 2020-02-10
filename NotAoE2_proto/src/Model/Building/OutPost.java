package Model.Building;

public class OutPost extends Buildings{
    public OutPost (int PosX, int PosY, int buildingIndex) {
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 6;
         this.health = 200;
         this.score = 1;
         this.woodCost = 120;
         this.goldCost = 60;
         
         this.attack = 20;
         this.range = 4;
    }
}
