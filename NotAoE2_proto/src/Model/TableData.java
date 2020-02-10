package Model;

/**
 *
 * @author Alex
 */
public class TableData {
    
    private Item item;
    private int type;
    private int index;
    
    public TableData(){}
    
    public TableData(Item item){
        this.item = item;
    }
    
    public TableData(Item item, int type, int index){
        this.item = item;
        this.type = type;
        this.index = index;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    
}
