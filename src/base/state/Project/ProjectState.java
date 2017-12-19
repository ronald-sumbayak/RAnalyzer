package base.state.Project;

public interface ProjectState {
    
    void setSaved ();
    void setUnsaved ();
    void doAction ();
    void onEntry ();
    void onExit ();
}
