package dependency;

public class Dependency {
    
    private Requirement src, dst;
    
    public Dependency (Requirement src, Requirement dst) {
        this.src = src;
        this.dst = dst;
    }
    
    public Requirement getSrc () {
        return src;
    }
    
    public void setSrc (Requirement src) {
        this.src = src;
    }
    
    public Requirement getDst () {
        return dst;
    }

    public void setDst (Requirement dst) {
        this.dst = dst;
    }
}
