//package base.state.Button;
//
//public class Pressed implements ButtonState {
//
//    private Button context;
//
//    public Pressed (Button context) {
//        this.context = context;
//    }
//
//    @Override
//    public void setPressed () {
//
//    }
//
//    @Override
//    public void setUnpressed () {
//        context.changeState (new Unpressed (context));
//    }
//
//    @Override
//    public void doAction () {
//
//    }
//
//    @Override
//    public void onEntry () {
//        context.click ();
//    }
//
//    @Override
//    public void onExit () {
//
//    }
//}
