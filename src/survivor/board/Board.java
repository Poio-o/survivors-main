package survivor.board;

import survivor.elements.Element;

public class Board {
    private final int width;
    private final int height;
    
    public Board(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void moveTo(int x, int y){
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df",escCode,x,y));
    }

    public void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void draw(Element element){
        moveTo(element.getY(), element.getX());
        System.out.print(element);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

 
}
