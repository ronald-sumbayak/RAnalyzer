package ra.sumbayak.ranalyzer.base.state.Button;

public interface ButtonState {
    
    void setPressed ();
    void setUnpressed ();
    void doAction ();
    void onEntry ();
    void onExit ();
}
