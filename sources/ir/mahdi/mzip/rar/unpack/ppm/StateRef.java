package ir.mahdi.mzip.rar.unpack.ppm;

public class StateRef {
    private int freq;
    private int successor;
    private int symbol;

    public int getSymbol() {
        return this.symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol & 255;
    }

    public int getFreq() {
        return this.freq;
    }

    public void setFreq(int freq) {
        this.freq = freq & 255;
    }

    public void incFreq(int dFreq) {
        this.freq = (this.freq + dFreq) & 255;
    }

    public void decFreq(int dFreq) {
        this.freq = (this.freq - dFreq) & 255;
    }

    public void setValues(State statePtr) {
        setFreq(statePtr.getFreq());
        setSuccessor(statePtr.getSuccessor());
        setSymbol(statePtr.getSymbol());
    }

    public int getSuccessor() {
        return this.successor;
    }

    public void setSuccessor(int successor) {
        this.successor = successor;
    }

    public void setSuccessor(PPMContext successor) {
        setSuccessor(successor.getAddress());
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("State[");
        buffer.append("\n  symbol=");
        buffer.append(getSymbol());
        buffer.append("\n  freq=");
        buffer.append(getFreq());
        buffer.append("\n  successor=");
        buffer.append(getSuccessor());
        buffer.append("\n]");
        return buffer.toString();
    }
}
