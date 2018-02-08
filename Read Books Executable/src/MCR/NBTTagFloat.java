package MCR;



import java.io.*;

// Referenced classes of package net.minecraft.src:
//            NBTBase

public class NBTTagFloat extends NBTBase
{

    public NBTTagFloat()
    {
    }

    public NBTTagFloat(float f)
    {
        floatValue = f;
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        dataoutput.writeFloat(floatValue);
    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        floatValue = datainput.readFloat();
    }

    public byte getType()
    {
        return 5;
    }

    public String toString()
    {
        return (new StringBuilder()).append("").append(floatValue).toString();
    }

    public float floatValue;
}
