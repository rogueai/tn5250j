package org.tn5250j.event.order;

public abstract class AbstractGraphicOrder implements IGraphicOrder {
    private OrderCode code;

    private int[] data = new int[]{};

    public AbstractGraphicOrder(OrderCode code) {
        this.code = code;
    }

    protected boolean isComplete() {
        if (code.variableLength) {
//            return (data.length - 1) % dataLength == 0;
            return data[data.length - 1] == OrderCode.EndOfData.code;
        }
        return data.length == code.dataLength;
    }

}
