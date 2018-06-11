package ra.sumbayak.ranalyzer.entity;

import org.w3c.dom.*;

public class Statement {
    
    private String code, statement;
    private UseCase useCase;
    
    public Statement (String code, String statement, UseCase useCase) {
        this.code = code;
        edit (statement, useCase);
    }
    
    public String getCode () {
        return code;
    }
    
    public String getStatement () {
        return statement;
    }
    
    public UseCase getUseCase () {
        return useCase;
    }
    
    public void edit (String statement, UseCase useCase) {
        this.statement = statement;
        this.useCase = useCase;
    }
    
    public void write (Document doc, Element root) {
        Element s = doc.createElement ("Statement");
        s.setAttribute ("code", code);
        s.setAttribute ("uc", useCase.getCode ());
        s.setTextContent (statement);
        root.appendChild (s);
    }
}
