package ra.sumbayak.ranalyzer.entity;

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
}
