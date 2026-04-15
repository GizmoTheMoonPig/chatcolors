/*
 * Copyright (c) PORTB 2024
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package page.codeberg.portb.chatcolors;

import net.minecraft.ChatFormatting;

import java.util.HashMap;
import java.util.Map;

public class HumanReadableColorNames {
	private static final Map<String, ChatFormatting> FORMAT_CODES = new HashMap<>();

	public static boolean isValidCode(String code) {
		return FORMAT_CODES.containsKey(code);
	}

	public static char byCode(String code) {
		return FORMAT_CODES.get(code).getChar();
	}

	static {
		//colors that don't match the dark/light pattern
		FORMAT_CODES.put("black", ChatFormatting.BLACK);
		FORMAT_CODES.put("white", ChatFormatting.WHITE);
		FORMAT_CODES.put("blood", ChatFormatting.DARK_RED);
		FORMAT_CODES.put("orange", ChatFormatting.GOLD);
		FORMAT_CODES.put("gold", ChatFormatting.GOLD);

		//generate variations of the color names
		generateNamesForColor("blue", ChatFormatting.DARK_BLUE, ChatFormatting.BLUE);
		generateNamesForColor("green", ChatFormatting.DARK_GREEN, ChatFormatting.GREEN);
		generateNamesForColor("aqua", ChatFormatting.DARK_AQUA, ChatFormatting.AQUA);
		generateNamesForColor("cyan", ChatFormatting.DARK_AQUA, ChatFormatting.AQUA); //alternative name for aqua
		generateNamesForColor("red", ChatFormatting.DARK_RED, ChatFormatting.RED);
		generateNamesForColor("purple", ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE);
		generateNamesForColor("gray", ChatFormatting.DARK_GRAY, ChatFormatting.GRAY);
		generateNamesForColor("yellow", ChatFormatting.GOLD, ChatFormatting.YELLOW);

		//other formatting
		FORMAT_CODES.put("bold", ChatFormatting.BOLD);
		FORMAT_CODES.put("strong", ChatFormatting.BOLD);
		FORMAT_CODES.put("heavy", ChatFormatting.BOLD);
		FORMAT_CODES.put("b", ChatFormatting.BOLD);

		FORMAT_CODES.put("strikeout", ChatFormatting.STRIKETHROUGH);
		FORMAT_CODES.put("strikethrough", ChatFormatting.STRIKETHROUGH);
		FORMAT_CODES.put("strike", ChatFormatting.STRIKETHROUGH);
		FORMAT_CODES.put("s", ChatFormatting.STRIKETHROUGH);

		FORMAT_CODES.put("u", ChatFormatting.UNDERLINE);
		FORMAT_CODES.put("underline", ChatFormatting.UNDERLINE);

		FORMAT_CODES.put("i", ChatFormatting.ITALIC);
		FORMAT_CODES.put("italic", ChatFormatting.ITALIC);
		FORMAT_CODES.put("slanted", ChatFormatting.ITALIC);
		FORMAT_CODES.put("slant", ChatFormatting.ITALIC);

		FORMAT_CODES.put("obfuscated", ChatFormatting.OBFUSCATED);
		FORMAT_CODES.put("o", ChatFormatting.OBFUSCATED);

		FORMAT_CODES.put("reset", ChatFormatting.RESET);
		FORMAT_CODES.put("clear", ChatFormatting.RESET);
		FORMAT_CODES.put("default", ChatFormatting.RESET);
		FORMAT_CODES.put("r", ChatFormatting.RESET);
	}

	private static void generateNamesForColor(String colorName, ChatFormatting dark, ChatFormatting light) {
		FORMAT_CODES.put("deep " + colorName, dark);
		FORMAT_CODES.put("deep" + colorName, dark);
		FORMAT_CODES.put("deep_" + colorName, dark);

		FORMAT_CODES.put("dark " + colorName, dark);
		FORMAT_CODES.put("dark" + colorName, dark);
		FORMAT_CODES.put("dark_" + colorName, dark);

		FORMAT_CODES.put("d_" + colorName, dark);
		FORMAT_CODES.put("d " + colorName, dark);
		FORMAT_CODES.put("d" + colorName, dark);

		FORMAT_CODES.put(colorName + " dark", dark);
		FORMAT_CODES.put(colorName + "_dark", dark);
		FORMAT_CODES.put(colorName + "dark", dark);

		FORMAT_CODES.put(colorName + " deep", dark);
		FORMAT_CODES.put(colorName + "_deep", dark);
		FORMAT_CODES.put(colorName + "deep", dark);

		FORMAT_CODES.put(colorName + "_d", dark);
		FORMAT_CODES.put(colorName + " d", dark);
		FORMAT_CODES.put(colorName + "d", dark);

		FORMAT_CODES.put(colorName, light);

		FORMAT_CODES.put("light_" + colorName, light);
		FORMAT_CODES.put("light " + colorName, light);
		FORMAT_CODES.put("light" + colorName, light);

		FORMAT_CODES.put("bright_" + colorName, light);
		FORMAT_CODES.put("bright " + colorName, light);
		FORMAT_CODES.put("bright" + colorName, light);

		FORMAT_CODES.put(colorName + "_light", light);
		FORMAT_CODES.put(colorName + " light", light);
		FORMAT_CODES.put(colorName + "light", light);

		FORMAT_CODES.put(colorName + "_bright", light);
		FORMAT_CODES.put(colorName + " bright", light);
		FORMAT_CODES.put(colorName + "bright", light);
	}
}
