package org.tn5250j.graphics;

import org.tn5250j.graphics.model.Color;
import org.tn5250j.graphics.model.MarkerStyle;

/**
 * Holds information on the current graphics environment state for all such cases where stateful information is
 * required, for instance: whenever a Set Color order is issued, the color is stored in context to be reused by
 * subsequent graphic orders.
 */
public class GraphicsContext {

    private Color[] tableColor;

    private int colorIndex = 7;

    private MarkerStyle marker;

    private static GraphicsContext INSTANCE;


    private GraphicsContext() {

        this.tableColor = new Color[8];
        tableColor[0] = new Color(0, 0, 0);
        tableColor[1] = new Color(255, 0, 0);
        tableColor[2] = new Color(0, 255, 0);
        tableColor[3] = new Color(0, 0, 255);
        tableColor[4] = new Color(255, 0, 255);
        tableColor[5] = new Color(255, 255, 0);
        tableColor[6] = new Color(0, 255, 255);
        tableColor[7] = new Color(255, 255, 255);

        marker = MarkerStyle.BoxSolid;
    }

    public static GraphicsContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphicsContext();
        }
        return INSTANCE;
    }

    public void reset() {
        INSTANCE = null;
    }

    public void setTableColor(int idx, int r, int g, int b) {
        tableColor[idx] = new Color(getColorValue(r), getColorValue(g), getColorValue(b));
    }

    private int getColorValue(int i) {
        switch (i) {
            case 0:
                return 0;
            case 7:
                return 255;
            default:
                return i * 36;
        }
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public Color getColor() {
        return tableColor[colorIndex];
    }

    public void setMarker(int marker) {
        switch (marker) {
            case 1:
                this.marker = MarkerStyle.BoxSolidSmall;
                break;
            case 2:
                this.marker = MarkerStyle.BoxEmpty;
                break;
            case 3:
                this.marker = MarkerStyle.Plus;
                break;
            case 4:
                this.marker = MarkerStyle.Cross;
                break;
            case 5:
                this.marker = MarkerStyle.DiamondSolid;
                break;
            case 6:
                this.marker = MarkerStyle.DiamondHollow;
                break;
            case 7:
                this.marker = MarkerStyle.Asterisk3;
                break;
            case 8:
                this.marker = MarkerStyle.Asterisk5;
                break;
            case 0:
            default:
                this.marker = MarkerStyle.BoxSolid;
                break;
        }
    }

    public MarkerStyle getMarker() {
        return marker;
    }
}
