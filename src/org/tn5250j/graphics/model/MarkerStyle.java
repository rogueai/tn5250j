package org.tn5250j.graphics.model;

/**
 * Markers styles set by 0xB5 Set Marker Order.
 *
 * <pre>0100 aaaa</pre>
 */
public enum MarkerStyle {
    /**
     * 0000 Solid 5 x 5 box (default)
     */
    BoxSolid,
    /**
     * 0001 Solid 3 x 3 box
     */
    BoxSolidSmall,
    /**
     * 0010 Empty 5 x 5 box
     */
    BoxEmpty,
    /**
     * 0011 5 x 5 plus sign
     */
    Plus,
    /**
     * 0100 5 x 5 cross
     */
    Cross,
    /**
     * 0101 Solid 5 x 5 diamond
     */
    DiamondSolid,
    /**
     * 0110 Hollow 5 x 5 diamond
     */
    DiamondHollow,
    /**
     * 0111 3 segment asterisk
     */
    Asterisk3,
    /**
     * 1000 4 segment asterisk
     */
    Asterisk5

}
