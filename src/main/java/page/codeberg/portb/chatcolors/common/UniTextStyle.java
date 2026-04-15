/*
 * Copyright (c) PORTB 2024
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package page.codeberg.portb.chatcolors.common;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class UniTextStyle {
	private Style nativeStyle;

	public UniTextStyle(Style nativeStyle) {
		this.nativeStyle = nativeStyle;
	}

	public void toggleItalic() {
		this.nativeStyle = this.nativeStyle.withItalic(!this.nativeStyle.isItalic());
	}

	public void toggleBold() {
		this.nativeStyle = this.nativeStyle.withBold(!this.nativeStyle.isBold());
	}

	public void toggleStrikethrough() {
		this.nativeStyle = this.nativeStyle.withStrikethrough(!this.nativeStyle.isStrikethrough());
	}

	public void toggleUnderlined() {
		this.nativeStyle = this.nativeStyle.withUnderlined(!this.nativeStyle.isUnderlined());
	}

	public void setColor(String hexStr) {
		TextColor color = parseColor(hexStr);

		if (color == null)
			throw new InvalidHexCodeException();

		this.nativeStyle = this.nativeStyle.withColor(color);
	}

	private static TextColor parseColor(String hexStr) {
		return TextColor.parseColor(hexStr).result().orElse(null);
	}

	public void applyLegacyFormat(char code) {
		ChatFormatting format = ChatFormatting.getByCode(code);

		if (format != null)
			this.nativeStyle = this.nativeStyle.applyLegacyFormat(format);
	}

	public Style getNativeStyle() {
		return this.nativeStyle;
	}
}
