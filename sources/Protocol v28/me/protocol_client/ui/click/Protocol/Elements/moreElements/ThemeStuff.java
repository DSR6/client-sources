package me.protocol_client.ui.click.Protocol.Elements.moreElements;

import me.protocol_client.Protocol;
import me.protocol_client.ui.click.Protocol.Elements.Element;
import me.protocol_client.ui.click.Protocol.theme.ClickTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class ThemeStuff extends Element {
    protected static Minecraft mc = Minecraft.getMinecraft();
    protected final ClickTheme theme;

    public ThemeStuff(ClickTheme theme, float x, float y, float width, float height) {
        super(x, y, width, height);

        this.theme = theme;
    }

    @Override
    public void drawElement(int mouseX, int mouseY) {
        Protocol.getGuiClick().getTheme().renderButton(getTheme().getName(), Protocol.getGuiClick().getTheme() == getTheme(), getX(), getY(), getWidth(), getHeight(), isOverElement(mouseX, mouseY), this);
    }

    @Override
    public void keyPressed(int key) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isOverElement(mouseX, mouseY) && mouseButton == 0) {
            Protocol.getGuiClick().setTheme(theme);
        }
    }

    public ClickTheme getTheme() {
        return theme;
    }

    @Override
    public void onGuiClosed() {
    }
}
