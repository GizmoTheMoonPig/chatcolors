/*
 * Copyright (c) PORTB 2024
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package page.codeberg.portb.chatcolors.common;

import net.minecraft.util.FormattedCharSink;

public class UniCharacterConsumer {
	private final FormattedCharSink nativeConsumer;

	public UniCharacterConsumer(FormattedCharSink nativeConsumer) {
		this.nativeConsumer = nativeConsumer;
	}

	public boolean accept(int index, UniTextStyle style, int character) {
		return this.nativeConsumer.accept(index, style.getNativeStyle(), character);
	}
}
