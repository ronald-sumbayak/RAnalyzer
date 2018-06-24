package ra.sumbayak.ranalyzer.entity;

public class UseCaseDependency {
    
    public static final int ALTERNATIVE = 0;
    public static final int INCLUDE = 1;
    public static final int PRECONDITION = 2;
    public static final int EXTEND = 3;
    public static final int EXCEPTION = 4;
    
    private UseCase src, dst;
    private int type;
    
    UseCaseDependency (int type, UseCase src, UseCase dst) {
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
    
    public String getTypeText () {
        switch (type) {
            case ALTERNATIVE: return "Alternative";
            case INCLUDE: return "Include";
            case PRECONDITION: return "Pre-Condition";
            case EXTEND: return "Extend";
            case EXCEPTION: return "Exception";
            default: return "Unknown";
        }
    }
    
    @Override
    public String toString () {
        return String.format ("[%s:(%s:%s)]", getTypeText (), src.getName (), dst.getName ());
    }
}
