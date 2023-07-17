package org.tn5250j.graphics;

import org.tn5250j.event.order.DrawPolylineOrder;
import org.tn5250j.event.order.IGraphicOrder;
import org.tn5250j.event.order.WritePolymarkerOrder;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.tools.logging.TN5250jLogFactory;
import org.tn5250j.tools.logging.TN5250jLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for GDDM Graphics Data
 */
public class GraphicsDataParser {

    private final TN5250jLogger log = TN5250jLogFactory.getLogger(this.getClass());

    private Screen5250 screen;

    public GraphicsDataParser(Screen5250 screen) {
        // FIXME: remove dependency to Screen5250
        this.screen = screen;
    }

    public void parse(List<Byte> graphicsData) throws Exception {
        int scaleFactor = 2;

        for (int i = 0; i < graphicsData.size(); i++) {
            byte grByte = graphicsData.get(i);
            IGraphicOrder.OrderCode oc = IGraphicOrder.OrderCode.fromByte(grByte);
            if (oc == IGraphicOrder.OrderCode.Invalid) {
                log.error("Invalid order code: " + grByte);
            }
            log.debug(oc);
            switch (oc) {
                case DrawPolyline: // 0xA0 Draw Polyline
                    // 4 bytes: 0100 xxxx 01xx xxxx 0100 yyyy 01yy yyyy
                    // Processed until -110 0x92 End of Data
                    List<Integer> xPoints = new ArrayList<>();
                    List<Integer> yPoints = new ArrayList<>();
                    int nPoints = 0;
                    byte nextByte = -1;

                    while (nextByte != -110) {
                        List<Byte> coords = new ArrayList<>();
                        // skip
                        while (coords.size() < 4) {
                            Byte co = graphicsData.get(++i);
                            if (co != -111 && co != -112) {
                                coords.add(co);
                            }
                        }
                        byte x1 = coords.get(0);
                        byte x2 = coords.get(1);
                        byte y1 = coords.get(2);
                        byte y2 = coords.get(3);

                        // FIXME: find a better way to manipulate bits
                        String x1s = String.format("%8s", Integer.toBinaryString(x1 & 0xFF)).replace(' ', '0');
                        String x2s = String.format("%8s", Integer.toBinaryString(x2 & 0xFF)).replace(' ', '0');
                        String y1s = String.format("%8s", Integer.toBinaryString(y1 & 0xFF)).replace(' ', '0');
                        String y2s = String.format("%8s", Integer.toBinaryString(y2 & 0xFF)).replace(' ', '0');

                        String xs = x1s.substring(4) + x2s.substring(2);
                        String ys = y1s.substring(4) + y2s.substring(2);
                        int x = Integer.parseInt(xs, 2);
                        int y = Integer.parseInt(ys, 2);
                        xPoints.add(x * scaleFactor);
                        yPoints.add(y * scaleFactor);

                        nPoints++;
                        nextByte = graphicsData.get(i + 1);
                    }
                    IGraphicOrder order = new DrawPolylineOrder(xPoints.stream().mapToInt(Integer::intValue)
                            .toArray(), yPoints.stream().mapToInt(Integer::intValue).toArray(), nPoints);
                    log.debug(xPoints);
                    log.debug(yPoints);
                    screen.fireGraphicsOrder(order);
                    break;
                case WriteBackground: // 0xA3 Write Background
                    // 1 byte: 0100 0aaa
                    graphicsData.get(++i);
                    break;
                case WritePolymarker: // 0xA4 Write Polymarker
                    // 4 bytes: 0100 xxxx 01xx xxxx 0100 yyyy 01yy yyyy
                    // Processed until -110 0x92 End of Data
                    List<Integer> mxPoints = new ArrayList<>();
                    List<Integer> myPoints = new ArrayList<>();
                    byte mnextByte = -1;

                    while (mnextByte != -110) {
                        List<Byte> coords = new ArrayList<>();
                        // skip
                        while (coords.size() < 4) {
                            Byte co = graphicsData.get(++i);
                            if (co != -111 && co != -112) {
                                coords.add(co);
                            }
                        }
                        byte x1 = coords.get(0);
                        byte x2 = coords.get(1);
                        byte y1 = coords.get(2);
                        byte y2 = coords.get(3);

                        // FIXME: find a better way to manipulate bits
                        String x1s = String.format("%8s", Integer.toBinaryString(x1 & 0xFF)).replace(' ', '0');
                        String x2s = String.format("%8s", Integer.toBinaryString(x2 & 0xFF)).replace(' ', '0');
                        String y1s = String.format("%8s", Integer.toBinaryString(y1 & 0xFF)).replace(' ', '0');
                        String y2s = String.format("%8s", Integer.toBinaryString(y2 & 0xFF)).replace(' ', '0');

                        String xs = x1s.substring(4) + x2s.substring(2);
                        String ys = y1s.substring(4) + y2s.substring(2);
                        int x = Integer.parseInt(xs, 2);
                        int y = Integer.parseInt(ys, 2);
                        mxPoints.add(x * scaleFactor);
                        myPoints.add(y * scaleFactor);

                        mnextByte = graphicsData.get(i + 1);
                    }
                    IGraphicOrder morder = new WritePolymarkerOrder(mxPoints.stream().mapToInt(Integer::intValue)
                            .toArray(), myPoints.stream().mapToInt(Integer::intValue).toArray());
                    screen.fireGraphicsOrder(morder);
                    break;
                case SetColor: // 0xB0 Set Color
                    // 1 byte: 0100 0aaa
                    Byte colb = graphicsData.get(++i);
                    String cols = String.format("%8s", Integer.toBinaryString(colb & 0xFF)).replace(' ', '0');
                    cols = cols.substring(5);
                    int colIdx = Integer.parseInt(cols, 2);
                    GraphicsContext.getInstance().setColorIndex(colIdx);
                    break;
                case SetStyle: // 0xB1 Set Style
                    // 4 bytes: 0100 aaaa 0100 bbbb 0100 cccc 0100 dddd
                    graphicsData.get(++i);
                    graphicsData.get(++i);
                    graphicsData.get(++i);
                    graphicsData.get(++i);
                    screen.fireGraphicsOrder(IGraphicOrder.NOOP);
                    break;
                case SetStyleOffset: // 0xB2 Set Style Offset
                    // 1 byte: 01aa bbbb
                    graphicsData.get(++i);
                    screen.fireGraphicsOrder(IGraphicOrder.NOOP);
                    break;
                case SetFunction: // 0xB3 Set Function
                    // 1 byte: 0100 00aa
                    graphicsData.get(++i);
                    screen.fireGraphicsOrder(IGraphicOrder.NOOP);
                    break;
                case SetColorTable: // 0xB4 Set Color Table
                    // 3 bytes: 0100 0nnn 01rr rggg 01bbb000
                    // Processed until -110 0x92 End of Data
                    nextByte = -1;
                    while (nextByte != -110) {
                        List<Byte> colors = new ArrayList<>();
                        // skip
                        while (colors.size() < 3) {
                            Byte co = graphicsData.get(++i);
                            if (co != -111 && co != -112) {
                                colors.add(co);
                            }
                        }
                        byte idxb = colors.get(0);
                        byte c1b = colors.get(1);
                        byte c2b = colors.get(2);

                        // FIXME: find a better way to manipulate bits
                        String is = String.format("%8s", Integer.toBinaryString(idxb & 0xFF)).replace(' ', '0');
                        String c1s = String.format("%8s", Integer.toBinaryString(c1b & 0xFF)).replace(' ', '0');
                        String c2s = String.format("%8s", Integer.toBinaryString(c2b & 0xFF)).replace(' ', '0');

                        String idxs = is.substring(5);
                        String rs = c1s.substring(2, 5);
                        String gs = c1s.substring(5);
                        String bs = c2s.substring(2, 5);
                        int idx = Integer.parseInt(idxs, 2);
                        int r = Integer.parseInt(rs, 2);
                        int g = Integer.parseInt(gs, 2);
                        int b = Integer.parseInt(bs, 2);

                        GraphicsContext.getInstance().setTableColor(idx, r, g, b);
                        nextByte = graphicsData.get(i + 1);
                    }
                    break;
                case SetMarker: // 0xB5 Set Marker
                    // 1 byte: 0100 0aaa
                    Byte markb = graphicsData.get(++i);
                    String marks = String.format("%8s", Integer.toBinaryString(markb & 0xFF)).replace(' ', '0');
                    marks = marks.substring(4);
                    int marker = Integer.parseInt(marks, 2);
                    GraphicsContext.getInstance().setMarker(marker);
                    break;
                case SetLineWeight: // 0xB6 Set Line Weight
                    // 1 byte: 0100 000a
                    // 0: single width
                    // 1: double width
                    graphicsData.get(++i);
                    screen.fireGraphicsOrder(IGraphicOrder.NOOP);
                    break;
                case ReadStatus: // 0x80 Read Status
                    // 2 bytes: 01aa aaaa 01aa aaaa
                    graphicsData.get(++i);
                    graphicsData.get(++i);
                    // FIXME: hardcoded, retrieve from actual bytes
                    screen.setCursor(1, 4);
                    screen.fireGraphicsOrder(IGraphicOrder.NOOP);
                    break;
                case EndGraphicsBlock: // 0x90 End Graphics Block
                    // skip till the last 0x90
                    i = skipToLast(graphicsData, i, -112);
                    break;
                case MoreDataToCome: // 0x91 More Data to Come
                    break;
                case EndOfData: // 0x92 End of Data
                    break;
                case GraphicsDisplayOn: // 0x93 Graphics Display On
                    // skip till the last 0x93
                    i = skipToLast(graphicsData, i, -109);
                    break;
                case GraphicsDisplayOff: // 0x94 Graphics Display Off
                    // skip till the last 0x94
                    i = skipToLast(graphicsData, i, -108);
                    break;
                case EndGraphics: // 0x95 End Graphics
                    // skip till the last 0x90
                    i = skipToLast(graphicsData, i, -112);
                    break;
                case SuppressPacingResponse: // 0x96 Suppress Pacing Response
                    break;
                case LoadGraphicsMixTable: // 0xC3 Load Graphics Mix Table
                    // 2 bytes: 0100 0aaa 01000 bcmy (black cyan magenta yellow)
                    // Processed until -110 0x92 End of Data
                    i = skipTo(graphicsData, i, -110);
                    screen.fireGraphicsOrder(IGraphicOrder.NOOP);
                    break;
                case SetFillMode:
                    break;
                case DrawScanline:
                    break;
                case FillPolygon:
                    break;
                case DefineShieldArea:
                    break;
                case PrinterDataFollows:
                    break;
                case ScreenCopy:
                    break;
                case LoadColorMixTable:
                    break;
                case SetPrinterTimeOut:
                    break;
                case SetAddress:
                    break;
                case SetTalkerTimeOut:
                    break;
                case SetEOIMode:
                    break;
                case IEEEDataFollows:
                    break;
                case GoToStandby:
                    break;
                case RemoteEnable:
                    break;
                case RemoteDisable:
                    break;
                case InterfaceClear:
                    break;
                case Invalid:
                default:
                    log.debug(oc + " (UNHANDLED)");
                    break;
            }
        }
    }

    private static int skipToLast(List<Byte> graphicsData, int i, int toSkip) {
        while (i + 1 < graphicsData.size() && graphicsData.get(i + 1) == toSkip) {
            i++;
        }
        return i;
    }

    private static int skipTo(List<Byte> graphicsData, int i, int toSkip) {
        while (i + 1 < graphicsData.size() && graphicsData.get(i + 1) != toSkip) {
            i++;
        }
        return i;
    }


}
