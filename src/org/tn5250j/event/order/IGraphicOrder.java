package org.tn5250j.event.order;

import java.awt.*;

public interface IGraphicOrder {

    /**
     * Enum for order codes. Values can be derived from unsigned bytes with {{@link #fromByte(int)}}
     * <p></p>
     * Note that in Java, bytes are represented internally as signed int in the range [-128, +127] Order values are
     * instead unsigned bytes [0, 255]
     */
    public enum OrderCode {
        // Invalid Orders: 0b00aa_aaaa
        Invalid((byte) 0, 0, false),
        // Graphic Data: 0b01aa_aaaa
        // Graphic Control Orders: 0b1001_aaaa
        EndGraphicsBlock((byte) (0x90 & 0xFF), 0, false),
        MoreDataToCome((byte) (0x91 & 0xFF), 0, false),
        EndOfData((byte) (0x92 & 0xFF), 0, false),
        GraphicsDisplayOn((byte) (0x93 & 0xFF), 0, false),
        GraphicsDisplayOff((byte) (0x94 & 0xFF), 0, false),
        EndGraphics((byte) (0x95 & 0xFF), 0, false),
        SuppressPacingResponse((byte) (0x96 & 0xFF), 0, false),
        // Graphic Set Orders: 0b1011_aaaa
        SetColorTable((byte) (0xB4 & 0xFF), 3, true),
        SetColor((byte) (0xB0 & 0xFF), 1, false),
        SetStyle((byte) (0xB1 & 0xFF), 4, false),
        SetStyleOffset((byte) (0xB2 & 0xFF), 1, false),
        SetFunction((byte) (0xB3 & 0xFF), 1, false),
        SetMarker((byte) (0xB5 & 0xFF), 1, false),
        SetLineWeight((byte) (0xB6 & 0xFF), 1, false),
        SetFillMode((byte) (0xB & 0xFF), 2, false),
        // Graphic Draw Orders: 0b1010_aaaa
        WriteBackground((byte) (0xA3 & 0xFF), 1, false),
        DrawPolyline((byte) (0xA0 & 0xFF), 4, true),
        DrawScanline((byte) (0xA1 & 0xFF), 5, true),
        WritePolymarker((byte) (0xA4 & 0xFF), 4, true),
        FillPolygon((byte) (0xA5 & 0xFF), 4, true),
        DefineShieldArea((byte) (0xA6 & 0xFF), 4, true),
        // Graphic Read Orders: 0b1000_aaaa
        ReadStatus((byte) (0x80 & 0xFF), 2, false),
        // Feature Printer Orders: 0b1100_aaaa
        PrinterDataFollows((byte) (0xC0 & 0xFF), 1, true),
        ScreenCopy((byte) (0xC1 & 0xFF), 0, false),
        LoadColorMixTable((byte) (0xC2 & 0xFF), 2, true),
        LoadGraphicsMixTable((byte) (0xC3 & 0xFF), 2, true),
        SetPrinterTimeOut((byte) (0xC4 & 0xFF), 1, false),
        // Feature IEEE Set Orders: 0b1101_aaaa
        SetAddress((byte) (0xD11 & 0xFF), 1, false),
        SetTalkerTimeOut((byte) (0xD2 & 0xFF), 2, false),
        SetEOIMode((byte) (0xD3 & 0xFF), 1, false),
        // Feature IEEE Local Command Orders: 0b1110_aaaa
        IEEEDataFollows((byte) (0xE0 & 0xFF), 1, true),
        GoToStandby((byte) (0xE2 & 0xFF), 0, false),
        RemoteEnable((byte) (0xE3 & 0xFF), 0, false),
        RemoteDisable((byte) (0xE4 & 0xFF), 0, false),

        InterfaceClear((byte) (0xE5 & 0xFF), 0, false);
        // Feature Reserved Orders: 0b1111_aaaa

        public final byte code;

        /**
         * The number of bytes representing the data length of the Graphic Order. To be a valid order, the data array
         * must be of exactly {@link #dataLength} bytes, except for variable length data orders.
         */
        public final int dataLength;

        /**
         * Graphic Command accepting multiple data chunks: variable length data orders allow for multiple blocks
         * spanning and must be terminated by an {@link #EndOfData}}order 0x92.
         * <p>
         * Some graphic orders are only considered valid if data length is: n * dataLength + 1 (taking into account
         * 0x92). For instance, {@link #DrawPolyline} can only be performed on 4-byte chunks.
         * <p>
         * Note that this rule is not applicable to all graphic order though: some orders would accept a
         * #{@link #dataLength}-bytes definition, plus an indefinite number of trailing bytes up till reaching
         * {@link #EndOfData}.
         * <p>
         * Incomplete variable data orders can occur: as a graphic message can have a maximum size of 256 bytes,
         * variable data orders can be split across multiple messages via the {@link #MoreDataToCome} (0x91) order.
         */
        public final boolean variableLength;

        OrderCode(byte code, int dataLength, boolean variableLength) {
            // in Java, bytes are represented internally as signed int
            this.code = code;
            this.dataLength = dataLength;
            this.variableLength = variableLength;
        }

        public static OrderCode fromByte(int b) {
            for (OrderCode o : OrderCode.values()) {
                if (o.code == b) {
                    return o;
                }
            }
            return Invalid;
        }


        @Override
        public String toString() {
            return "[" + String.format("0x%02X", code) + "] " + name();
        }
    }

    IGraphicOrder NOOP = g -> {
        // NOOP order
    };

    void execute(Graphics2D g);

}
