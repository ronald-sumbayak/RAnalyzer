package dependency;

import document.UseCase;

public class Requirement {
    
    private UseCase useCase;
    
    public Requirement (UseCase useCase) {
        this.useCase = useCase;
    }
    
    public UseCase getUseCase () {
        return useCase;
    }

    public void setUseCase (UseCase useCase) {
        this.useCase = useCase;
    }
}
