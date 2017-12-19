package entity;

public class Requirement {
    
    private Statement statement;
    
    public Requirement (Statement statement) {
        this.statement = statement;
    }
    
    public Statement getStatement () {
        return statement;
    }
}
