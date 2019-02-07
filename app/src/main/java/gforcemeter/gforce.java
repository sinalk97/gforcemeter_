package gforcemeter;

public class gforce {
    private MainActivity controller;
    public gforce(MainActivity controller){
        this.controller = controller;
    }

    public void reset(){
        this.controller.reset();
    }
}
