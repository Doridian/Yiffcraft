package wecui.event.cui;

import wecui.WorldEditCUI;
import wecui.render.CUIRegion;
import wecui.render.CuboidRegion;
import wecui.render.Polygon2DRegion;

/**
 * Called when selection event is received
 * 
 * @author lahwran
 * @author yetanotherx
 */
public class CUISelectionEvent extends CUIBaseEvent {

    public CUISelectionEvent(WorldEditCUI controller, String[] args) {
        super(controller, args);
    }

    @Override
    public CUIEventType getEventType() {
        return CUIEventType.SELECTION;
    }

    @Override
    public String run() {

        CUIRegion newRegion = null;
        
        if (this.getString(0).equals("cuboid")) {
            
            newRegion = new CuboidRegion(controller);
            newRegion.initialize();
            
        } else if (this.getString(0).equals("polygon2d")) {
            
            newRegion = new Polygon2DRegion(controller);
            newRegion.initialize();
            
        } else {
            return "Invalid selection type. Must be either cuboid or polygon2d.";
        }
        
        controller.setSelection(newRegion);
        controller.getDebugger().debug("Received selection event, initalizing new region instance.");

        return null;
    }
}
