package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.MathHelper;

public class NBTTagFloat extends NBTBase.NBTPrimitive {
   private float data;

   NBTTagFloat() {
   }

   public NBTTagFloat(float data) {
      this.data = data;
   }

   public boolean equals(Object p_equals_1_) {
      if(super.equals(p_equals_1_)) {
         NBTTagFloat nbttagfloat = (NBTTagFloat)p_equals_1_;
         return this.data == nbttagfloat.data;
      } else {
         return false;
      }
   }

   public String toString() {
      return "" + this.data + "f";
   }

   public int hashCode() {
      return super.hashCode() ^ Float.floatToIntBits(this.data);
   }

   public byte getByte() {
      return (byte)(MathHelper.floor_float(this.data) & 255);
   }

   public short getShort() {
      return (short)(MathHelper.floor_float(this.data) & '\uffff');
   }

   public int getInt() {
      return MathHelper.floor_float(this.data);
   }

   public long getLong() {
      return (long)this.data;
   }

   public float getFloat() {
      return this.data;
   }

   public double getDouble() {
      return (double)this.data;
   }

   void write(DataOutput output) throws IOException {
      output.writeFloat(this.data);
   }

   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
      sizeTracker.read(96L);
      this.data = input.readFloat();
   }

   public byte getId() {
      return (byte)5;
   }

   public NBTBase copy() {
      return new NBTTagFloat(this.data);
   }
}
