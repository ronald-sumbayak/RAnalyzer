package entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UseCaseDependency {
    
    public static final int TYPE_INCLUDE = 0;
    public static final int TYPE_EXTEND = 1;
    
    private UseCase src, dst;
    private int type;
    
    public void connect (int type, UseCase src, UseCase dst) {
        this.type = type;
        this.src = src;
        this.dst = dst;
    }
    
    public UseCase getSrc () {
        return src;
    }
    
    public UseCase getDst () {
        return dst;
    }
    
    public int getType () {
        return type;
    }
    
    public void write (Document doc, Element root) {
        Element ucDependency = doc.createElement ("UseCaseDependency");
        ucDependency.setAttribute ("type", String.valueOf (type));
        ucDependency.setAttribute ("src", src.getCode ());
        ucDependency.setAttribute ("dst", dst.getCode ());
        root.appendChild (ucDependency);
    }
}
