package ra.sumbayak.ranalyzer.base.state.project;

import ra.sumbayak.ranalyzer.entity.Project;

public class Unsaved implements ProjectState {
    
    private Project context;
    
    public Unsaved (Project context) {
        this.context = context;
    }
    
    @Override
    public void setSaved () {
        context.changeState (new Saved (context));
    }
    
    @Override
    public void setUnsaved () {
    
    }
}
