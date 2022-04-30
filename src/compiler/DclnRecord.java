package compiler;

public class DclnRecord {
    public String name;
    public int addr;
    public boolean isConst;
    public String type;

    public DclnRecord(String name, int addr, boolean isConst, String type) {
        this.name = name;
        this.addr = addr;
        this.isConst = isConst;
        this.type = type;
    }
}
