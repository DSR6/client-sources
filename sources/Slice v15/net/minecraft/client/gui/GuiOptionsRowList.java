package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;

public class GuiOptionsRowList extends GuiListExtended
{
  private final List field_148184_k = com.google.common.collect.Lists.newArrayList();
  private static final String __OBFID = "CL_00000677";
  
  public GuiOptionsRowList(Minecraft mcIn, int p_i45015_2_, int p_i45015_3_, int p_i45015_4_, int p_i45015_5_, int p_i45015_6_, GameSettings.Options... p_i45015_7_)
  {
    super(mcIn, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
    field_148163_i = false;
    
    for (int var8 = 0; var8 < p_i45015_7_.length; var8 += 2)
    {
      GameSettings.Options var9 = p_i45015_7_[var8];
      GameSettings.Options var10 = var8 < p_i45015_7_.length - 1 ? p_i45015_7_[(var8 + 1)] : null;
      GuiButton var11 = func_148182_a(mcIn, p_i45015_2_ / 2 - 155, 0, var9);
      GuiButton var12 = func_148182_a(mcIn, p_i45015_2_ / 2 - 155 + 160, 0, var10);
      field_148184_k.add(new Row(var11, var12));
    }
  }
  
  private GuiButton func_148182_a(Minecraft mcIn, int p_148182_2_, int p_148182_3_, GameSettings.Options p_148182_4_)
  {
    if (p_148182_4_ == null)
    {
      return null;
    }
    

    int var5 = p_148182_4_.returnEnumOrdinal();
    return p_148182_4_.getEnumFloat() ? new GuiOptionSlider(var5, p_148182_2_, p_148182_3_, p_148182_4_) : new GuiOptionButton(var5, p_148182_2_, p_148182_3_, p_148182_4_, Minecraft.gameSettings.getKeyBinding(p_148182_4_));
  }
  

  public Row func_180792_c(int p_180792_1_)
  {
    return (Row)field_148184_k.get(p_180792_1_);
  }
  
  protected int getSize()
  {
    return field_148184_k.size();
  }
  



  public int getListWidth()
  {
    return 400;
  }
  
  protected int getScrollBarX()
  {
    return super.getScrollBarX() + 32;
  }
  



  public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_)
  {
    return func_180792_c(p_148180_1_);
  }
  
  public static class Row implements GuiListExtended.IGuiListEntry
  {
    private final Minecraft field_148325_a = Minecraft.getMinecraft();
    private final GuiButton field_148323_b;
    private final GuiButton field_148324_c;
    private static final String __OBFID = "CL_00000678";
    
    public Row(GuiButton p_i45014_1_, GuiButton p_i45014_2_)
    {
      field_148323_b = p_i45014_1_;
      field_148324_c = p_i45014_2_;
    }
    
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
      if (field_148323_b != null)
      {
        field_148323_b.yPosition = y;
        field_148323_b.drawButton(field_148325_a, mouseX, mouseY);
      }
      
      if (field_148324_c != null)
      {
        field_148324_c.yPosition = y;
        field_148324_c.drawButton(field_148325_a, mouseX, mouseY);
      }
    }
    
    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
      if (field_148323_b.mousePressed(field_148325_a, p_148278_2_, p_148278_3_))
      {
        if ((field_148323_b instanceof GuiOptionButton))
        {
          Minecraft.gameSettings.setOptionValue(((GuiOptionButton)field_148323_b).returnEnumOptions(), 1);
          field_148323_b.displayString = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(field_148323_b.id));
        }
        
        return true;
      }
      if ((field_148324_c != null) && (field_148324_c.mousePressed(field_148325_a, p_148278_2_, p_148278_3_)))
      {
        if ((field_148324_c instanceof GuiOptionButton))
        {
          Minecraft.gameSettings.setOptionValue(((GuiOptionButton)field_148324_c).returnEnumOptions(), 1);
          field_148324_c.displayString = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(field_148324_c.id));
        }
        
        return true;
      }
      

      return false;
    }
    

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
      if (field_148323_b != null)
      {
        field_148323_b.mouseReleased(x, y);
      }
      
      if (field_148324_c != null)
      {
        field_148324_c.mouseReleased(x, y);
      }
    }
    
    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
  }
}
