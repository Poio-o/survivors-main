package survivor.board;

import javax.lang.model.element.Element;

public class Cell {
    private final Element element;

    public Cell(){
        this.element = null;
    }

    public Cell(Element element) {
        this.element = element;
    }
    
    public Element getElement() {
        return element;
    }
}
 