package com.darkcart.xdolf.mods.render;

import org.lwjgl.input.Keyboard;

import com.darkcart.xdolf.Module;
import com.darkcart.xdolf.util.Category;

public class rainbowClickGui extends Module {

	public rainbowClickGui() {
		super("rainbowClickGui", "New", "Makes all the ClickGUI buttons rainbow.", Keyboard.KEYBOARD_SIZE, 0xFFFFFF, Category.RENDER);
	}
}