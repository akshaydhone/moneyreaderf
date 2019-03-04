package ir.mahdi.mzip.rar.unpack.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class VMPreparedProgram {
    private List<VMPreparedCommand> AltCmd;
    private List<VMPreparedCommand> Cmd;
    private int CmdCount;
    private int FilteredDataOffset;
    private int FilteredDataSize;
    private Vector<Byte> GlobalData;
    private int[] InitR;
    private Vector<Byte> StaticData;

    public VMPreparedProgram() {
        this.Cmd = new ArrayList();
        this.AltCmd = new ArrayList();
        this.GlobalData = new Vector();
        this.StaticData = new Vector();
        this.InitR = new int[7];
        this.AltCmd = null;
    }

    public List<VMPreparedCommand> getAltCmd() {
        return this.AltCmd;
    }

    public void setAltCmd(List<VMPreparedCommand> altCmd) {
        this.AltCmd = altCmd;
    }

    public List<VMPreparedCommand> getCmd() {
        return this.Cmd;
    }

    public void setCmd(List<VMPreparedCommand> cmd) {
        this.Cmd = cmd;
    }

    public int getCmdCount() {
        return this.CmdCount;
    }

    public void setCmdCount(int cmdCount) {
        this.CmdCount = cmdCount;
    }

    public int getFilteredDataOffset() {
        return this.FilteredDataOffset;
    }

    public void setFilteredDataOffset(int filteredDataOffset) {
        this.FilteredDataOffset = filteredDataOffset;
    }

    public int getFilteredDataSize() {
        return this.FilteredDataSize;
    }

    public void setFilteredDataSize(int filteredDataSize) {
        this.FilteredDataSize = filteredDataSize;
    }

    public Vector<Byte> getGlobalData() {
        return this.GlobalData;
    }

    public void setGlobalData(Vector<Byte> globalData) {
        this.GlobalData = globalData;
    }

    public int[] getInitR() {
        return this.InitR;
    }

    public void setInitR(int[] initR) {
        this.InitR = initR;
    }

    public Vector<Byte> getStaticData() {
        return this.StaticData;
    }

    public void setStaticData(Vector<Byte> staticData) {
        this.StaticData = staticData;
    }
}
