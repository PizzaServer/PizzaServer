package io.github.willqi.pizzaserver.server.network.protocol.packets;

public class LevelChunkPacket extends ImplBedrockPacket {

    public static final int ID = 0x3a;

    private int x;
    private int z;
    private int subChunkCount;
    private byte[] data = new byte[0];


    public LevelChunkPacket() {
        super(ID);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getSubChunkCount() {
        return this.subChunkCount;
    }

    public void setSubChunkCount(int subChunkCount){
        this.subChunkCount = subChunkCount;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }



}
