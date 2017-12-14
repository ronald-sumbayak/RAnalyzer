package document;

public class UCDependency {

    private UseCase src;
    private UseCase dst;
    
    public UCDependency (UseCase src, UseCase dst) {
        this.src = src;
        this.dst = dst;
    }
    
    public UseCase getSrc () {
        return src;
    }
    
    public void setSrc (UseCase src) {
        this.src = src;
    }
    
    public UseCase getDst () {
        return dst;
    }
    
    public void setDst (UseCase dst) {
        this.dst = dst;
    }
}
