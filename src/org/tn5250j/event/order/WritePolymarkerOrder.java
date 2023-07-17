package org.tn5250j.event.order;

import org.tn5250j.graphics.GraphicsContext;
import org.tn5250j.graphics.model.MarkerStyle;
import org.tn5250j.tools.logging.TN5250jLogFactory;
import org.tn5250j.tools.logging.TN5250jLogger;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * Draws different marker shapes.
 * <p>
 * FIXME: there needs to be a proper way to handle scaling instead of all this hardcoded madness
 */
public class WritePolymarkerOrder implements IGraphicOrder {

    private final TN5250jLogger logger = TN5250jLogFactory.getLogger(this.getClass());

    private static final int scaleFactor = 2;
    private int[] xPoints;

    private int[] yPoints;


    public WritePolymarkerOrder(int[] xPoints, int[] yPoints) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
    }

    public void execute(Graphics2D g) {
        // FIXME flip y-axis, move to a centralize G2D manager
        Rectangle bounds = g.getDeviceConfiguration().getBounds();
        AffineTransform tform = AffineTransform.getTranslateInstance(0, bounds.height);
        tform.scale(1, -1);
        g.setTransform(tform);

        org.tn5250j.graphics.model.Color color = GraphicsContext.getInstance().getColor();
        MarkerStyle markerStyle = GraphicsContext.getInstance().getMarker();
        g.setColor(new Color(color.getR(), color.getG(), color.getB()));
        g.setStroke(new BasicStroke(2F));

        // TODO: handle scaleFactor
        for (int i = 0; i < xPoints.length; i++) {
            int x = xPoints[i];
            int y = yPoints[i];
            switch (markerStyle) {
                case BoxSolidSmall:
                    g.fillRect(x - (1 * scaleFactor), y - (1 * scaleFactor), 3 * scaleFactor, 3 * scaleFactor);
                    break;
                case BoxEmpty:
                    g.drawRect(x - (2 * scaleFactor), y - (2 * scaleFactor), 5 * scaleFactor, 5 * scaleFactor);
                    break;
                case Plus:
                    g.drawLine(x - (2 * scaleFactor), y, x + (2 * scaleFactor), y);
                    g.drawLine(x, y - (2 * scaleFactor), x, y + (2 * scaleFactor));
                    break;
                case Cross:
                    g.drawLine(x - (2 * scaleFactor), y + (2 * scaleFactor), x + (2 * scaleFactor), y - (2 * scaleFactor));
                    g.drawLine(x - (2 * scaleFactor), y - (2 * scaleFactor), x + (2 * scaleFactor), y + (2 * scaleFactor));
                    break;
                case DiamondSolid:
                    Shape d1 = createDiamond(x, y);
                    g.fill(d1);
                    g.draw(d1);
                    break;
                case DiamondHollow:
                    Shape d2 = createDiamond(x, y);
                    g.draw(d2);
                    break;
                case Asterisk3:
                    g.drawLine(x - (2 * scaleFactor), y + (2 * scaleFactor), x + (2 * scaleFactor), y - (2 * scaleFactor));
                    g.drawLine(x - (2 * scaleFactor), y - (2 * scaleFactor), x + (2 * scaleFactor), y + (2 * scaleFactor));
                    g.drawLine(x, y - (2 * scaleFactor), x, y + (2 * scaleFactor));
                    break;
                case Asterisk5:
                    g.drawLine(x - (2 * scaleFactor), y + (2 * scaleFactor), x + (2 * scaleFactor), y - (2 * scaleFactor));
                    g.drawLine(x - (2 * scaleFactor), y - (2 * scaleFactor), x + (2 * scaleFactor), y + (2 * scaleFactor));
                    g.drawLine(x - (2 * scaleFactor), y, x + (2 * scaleFactor), y);
                    g.drawLine(x, y - (2 * scaleFactor), x, y + (2 * scaleFactor));
                    break;
                case BoxSolid:
                    g.fillRect(x - (2 * 2), y - (2 * 2), 5 * 2, 5 * 2);
                default:
                    break;
            }
        }
    }

    private Shape createDiamond(int x, int y) {
        DiamondShape diamond = new DiamondShape(10, 10);
        // translate shape local coordinates to world coordinates
        int dx = (x - diamond.getBounds().width / 2);
        int dy = (y - diamond.getBounds().height / 2);
        AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
        return at.createTransformedShape(diamond);
    }

    private class DiamondShape extends Path2D.Double {

        public DiamondShape(int width, int height) {
            moveTo(width / 2, 0);
            lineTo(width, height / 2);
            lineTo(width / 2, height);
            lineTo(0, height / 2);
            closePath();
        }

    }


}
