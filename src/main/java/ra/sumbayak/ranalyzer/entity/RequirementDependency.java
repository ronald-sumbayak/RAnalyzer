package ra.sumbayak.ranalyzer.entity;

public class RequirementDependency {
    
    private static final int SIMILAR=0, REQUIRES=1, OR=2, TEMPORAL=3, ELABORATES=4, GENERALISES=5;
    private Statement src, dst;
    private int type;
    
    RequirementDependency (int type, Statement src, Statement dst) {
        this.type = type;
        this.src = src;
        this.dst = dst;
    }
    
    public Statement getSrc () {
        return src;
    }
    
    public Statement getDst () {
        return dst;
    }
    
    public int getType () {
        return type;
    }
    
    public String getTypeText () {
        switch (type) {
            case SIMILAR: return "Similar";
            case REQUIRES: return "Requires";
            case OR: return "Or";
            case TEMPORAL: return "Temporal";
            case ELABORATES: return "Elaborates";
            case GENERALISES: return "Generalises";
            default: return "Unknown";
        }
    }
}
