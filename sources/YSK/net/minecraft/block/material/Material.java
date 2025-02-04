package net.minecraft.block.material;

public class Material
{
    public static final Material redstoneLight;
    public static final Material sponge;
    public static final Material gourd;
    public static final Material portal;
    public static final Material barrier;
    public static final Material water;
    public static final Material sand;
    public static final Material piston;
    public static final Material grass;
    public static final Material leaves;
    public static final Material wood;
    public static final Material lava;
    public static final Material cloth;
    private boolean replaceable;
    public static final Material clay;
    public static final Material carpet;
    public static final Material packedIce;
    public static final Material craftedSnow;
    public static final Material air;
    public static final Material rock;
    public static final Material circuits;
    public static final Material tnt;
    public static final Material glass;
    public static final Material vine;
    public static final Material anvil;
    public static final Material cactus;
    public static final Material web;
    private final MapColor materialMapColor;
    private boolean isTranslucent;
    private boolean isAdventureModeExempt;
    public static final Material iron;
    public static final Material ground;
    private boolean canBurn;
    public static final Material ice;
    public static final Material fire;
    public static final Material dragonEgg;
    private boolean requiresNoTool;
    public static final Material cake;
    public static final Material plants;
    public static final Material coral;
    public static final Material snow;
    private int mobilityFlag;
    
    public boolean isReplaceable() {
        return this.replaceable;
    }
    
    public MapColor getMaterialMapColor() {
        return this.materialMapColor;
    }
    
    public boolean getCanBurn() {
        return this.canBurn;
    }
    
    private static String I(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        final char[] charArray = s2.toCharArray();
        int length = "".length();
        final char[] charArray2 = s.toCharArray();
        final int length2 = charArray2.length;
        int i = "".length();
        while (i < length2) {
            sb.append((char)(charArray2[i] ^ charArray[length % charArray.length]));
            ++length;
            ++i;
            "".length();
            if (3 < 2) {
                throw null;
            }
        }
        return sb.toString();
    }
    
    private Material setTranslucent() {
        this.isTranslucent = (" ".length() != 0);
        return this;
    }
    
    public Material(final MapColor materialMapColor) {
        this.requiresNoTool = (" ".length() != 0);
        this.materialMapColor = materialMapColor;
    }
    
    public boolean isSolid() {
        return " ".length() != 0;
    }
    
    public int getMaterialMobility() {
        return this.mobilityFlag;
    }
    
    public boolean isLiquid() {
        return "".length() != 0;
    }
    
    public boolean blocksLight() {
        return " ".length() != 0;
    }
    
    protected Material setImmovableMobility() {
        this.mobilityFlag = "  ".length();
        return this;
    }
    
    protected Material setAdventureModeExempt() {
        this.isAdventureModeExempt = (" ".length() != 0);
        return this;
    }
    
    protected Material setBurning() {
        this.canBurn = (" ".length() != 0);
        return this;
    }
    
    public boolean isToolNotRequired() {
        return this.requiresNoTool;
    }
    
    protected Material setNoPushMobility() {
        this.mobilityFlag = " ".length();
        return this;
    }
    
    public Material setReplaceable() {
        this.replaceable = (" ".length() != 0);
        return this;
    }
    
    protected Material setRequiresTool() {
        this.requiresNoTool = ("".length() != 0);
        return this;
    }
    
    public boolean blocksMovement() {
        return " ".length() != 0;
    }
    
    public boolean isOpaque() {
        int n;
        if (this.isTranslucent) {
            n = "".length();
            "".length();
            if (1 >= 2) {
                throw null;
            }
        }
        else {
            n = (this.blocksMovement() ? 1 : 0);
        }
        return n != 0;
    }
    
    static {
        air = new MaterialTransparent(MapColor.airColor);
        grass = new Material(MapColor.grassColor);
        ground = new Material(MapColor.dirtColor);
        wood = new Material(MapColor.woodColor).setBurning();
        rock = new Material(MapColor.stoneColor).setRequiresTool();
        iron = new Material(MapColor.ironColor).setRequiresTool();
        anvil = new Material(MapColor.ironColor).setRequiresTool().setImmovableMobility();
        water = new MaterialLiquid(MapColor.waterColor).setNoPushMobility();
        lava = new MaterialLiquid(MapColor.tntColor).setNoPushMobility();
        leaves = new Material(MapColor.foliageColor).setBurning().setTranslucent().setNoPushMobility();
        plants = new MaterialLogic(MapColor.foliageColor).setNoPushMobility();
        vine = new MaterialLogic(MapColor.foliageColor).setBurning().setNoPushMobility().setReplaceable();
        sponge = new Material(MapColor.yellowColor);
        cloth = new Material(MapColor.clothColor).setBurning();
        fire = new MaterialTransparent(MapColor.airColor).setNoPushMobility();
        sand = new Material(MapColor.sandColor);
        circuits = new MaterialLogic(MapColor.airColor).setNoPushMobility();
        carpet = new MaterialLogic(MapColor.clothColor).setBurning();
        glass = new Material(MapColor.airColor).setTranslucent().setAdventureModeExempt();
        redstoneLight = new Material(MapColor.airColor).setAdventureModeExempt();
        tnt = new Material(MapColor.tntColor).setBurning().setTranslucent();
        coral = new Material(MapColor.foliageColor).setNoPushMobility();
        ice = new Material(MapColor.iceColor).setTranslucent().setAdventureModeExempt();
        packedIce = new Material(MapColor.iceColor).setAdventureModeExempt();
        snow = new MaterialLogic(MapColor.snowColor).setReplaceable().setTranslucent().setRequiresTool().setNoPushMobility();
        craftedSnow = new Material(MapColor.snowColor).setRequiresTool();
        cactus = new Material(MapColor.foliageColor).setTranslucent().setNoPushMobility();
        clay = new Material(MapColor.clayColor);
        gourd = new Material(MapColor.foliageColor).setNoPushMobility();
        dragonEgg = new Material(MapColor.foliageColor).setNoPushMobility();
        portal = new MaterialPortal(MapColor.airColor).setImmovableMobility();
        cake = new Material(MapColor.airColor).setNoPushMobility();
        web = new Material() {
            private static String I(final String s, final String s2) {
                final StringBuilder sb = new StringBuilder();
                final char[] charArray = s2.toCharArray();
                int length = "".length();
                final char[] charArray2 = s.toCharArray();
                final int length2 = charArray2.length;
                int i = "".length();
                while (i < length2) {
                    sb.append((char)(charArray2[i] ^ charArray[length % charArray.length]));
                    ++length;
                    ++i;
                    "".length();
                    if (-1 >= 4) {
                        throw null;
                    }
                }
                return sb.toString();
            }
            
            @Override
            public boolean blocksMovement() {
                return "".length() != 0;
            }
        }.setRequiresTool().setNoPushMobility();
        piston = new Material(MapColor.stoneColor).setImmovableMobility();
        barrier = new Material(MapColor.airColor).setRequiresTool().setImmovableMobility();
    }
}
