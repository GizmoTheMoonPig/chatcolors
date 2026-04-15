/*
 * Copyright (c) PORTB 2024
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package page.codeberg.portb.chatcolors.mixin.client;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import page.codeberg.portb.chatcolors.OverhauledTextFormatter;
import page.codeberg.portb.chatcolors.common.UniCharacterConsumer;
import page.codeberg.portb.chatcolors.common.UniTextStyle;

@Mixin(StringDecomposer.class)
public class TextProcessingMixin {
	/**
	 * @author PORTB
	 * @reason Overhauls the game's text formatting syntax from 2009 (!)
	 */
	@Overwrite(remap = false)
	public static boolean iterateFormatted(String string, int startIndex, Style initialStyle, Style resetStyle, FormattedCharSink consumer) {
		return OverhauledTextFormatter.format(string, startIndex, new UniTextStyle(initialStyle), new UniTextStyle(resetStyle), new UniCharacterConsumer(consumer));
	}
}
