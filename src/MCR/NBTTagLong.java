package MCR;



import java.io.*;

// Referenced classes of package net.minecraft.src:
//            NBTBase

public class NBTTagLong extends NBTBase
{

    public NBTTagLong()
    {
    }

    public NBTTagLong(long l)
    {
        longValue = l;
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        dataoutput.writeLong(longValue);
    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        longValue = datainput.readLong();
    }

    public byte getType()
    {
        return 4;
    }

    public String toString()
    {
        return (new StringBuilder()).append("").append(longValue).toString();
    }

    public long longValue;
}
