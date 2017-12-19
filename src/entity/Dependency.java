package entity;

public class Dependency {
    
    private static final int SIMILAR=0, REQUIRES=1, OR=2, TEMPORAL=3, ELABORATES=4, GENERALISES=5;
    private Requirement src, dst;
    private int type;
    
    public Dependency (int type, Requirement src, Requirement dst) {
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
