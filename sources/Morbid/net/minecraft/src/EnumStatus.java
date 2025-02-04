package net.minecraft.src;

public enum EnumStatus
{
    OK("OK", 0), 
    NOT_POSSIBLE_HERE("NOT_POSSIBLE_HERE", 1), 
    NOT_POSSIBLE_NOW("NOT_POSSIBLE_NOW", 2), 
    TOO_FAR_AWAY("TOO_FAR_AWAY", 3), 
    OTHER_PROBLEM("OTHER_PROBLEM", 4), 
    NOT_SAFE("NOT_SAFE", 5);
    
    private EnumStatus(final String s, final int n) {
    }
}
