package base.state.Project;

import entity.Project;

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
    
    @Override
    public void doAction () {
    
    }
    
    @Override
    public void onEntry () {
    
    }
    
    @Override
    public void onExit () {
    
    }
}
