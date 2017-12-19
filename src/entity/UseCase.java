package entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UseCase {
    
    private String code, name;
    
    public UseCase (String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public String getCode () {
        return code;
    }
    
    public String getName () {
        return name;
    }
    
    public String getFullName () {
        return code + " - " + name;
    }
    
    public void write (Document doc, Element root) {
        Element uc = doc.createElement ("UseCase");
        uc.setAttribute ("code", code);
        uc.setAttribute ("name", name);
        root.appendChild (uc);
    }
}
