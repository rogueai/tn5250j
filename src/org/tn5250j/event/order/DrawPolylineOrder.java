package org.tn5250j.event.order;

import org.tn5250j.tools.logging.TN5250jLogFactory;
import org.tn5250j.tools.logging.TN5250jLogger;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;

public class DrawPolylineOrder implements IGraphicOrder {

    private final TN5250jLogger logger = TN5250jLogFactory.getLogger(this.getClass());

    private int[] xPoints;

    private int[] yPoints;

    private int nPoints;

    public DrawPolylineOrder(int[] xPoints, int[] yPoints, int nPoints) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.nPoints = nPoints;
    }

    public void execute(Graphics2D g) {
        // FIXME flip y-axis, move to a centralize G2D manager
        Rectangle bounds = g.getDeviceConfiguration().getBounds();
        AffineTransform tform = AffineTransform.getTranslateInstance(0, bounds.height);
        tform.scale(1, -1);
        g.setTransform(tform);

        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2F));

        g.drawPolyline(xPoints, yPoints, nPoints);
    }

}
