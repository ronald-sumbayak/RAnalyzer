package ra.sumbayak.ranalyzer.entity;

public class RequirementDependency {
    
    private static final int SIMILAR = 0;
    private static final int OR = 1;
    private static final int REQUIRES = 2;
    private static final int TEMPORAL = 3;
    private static final int ELABORATES = 4;
    private static final int GENERALISES = 5;
    
    private Requirement src, dst;
    private int type;
    
    RequirementDependency (int type, Requirement src, Requirement dst) {
        this.type = type;
        this.src = src;
        this.dst = dst;
    }
    
    public Requirement getSrc () {
        return src;
    }
    
    public Requirement getDst () {
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
