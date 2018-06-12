package ra.sumbayak.ranalyzer.base.state.project;

import ra.sumbayak.ranalyzer.entity.Project;

public class Saved implements ProjectState {
    
    private Project context;
    
    public Saved (Project context){
        this.context = context;
    }
    
    @Override
    public void setSaved () {
    
    }
    
    @Override
    public void setUnsaved () {
        context.changeState (new Unsaved (context));
    }
}
