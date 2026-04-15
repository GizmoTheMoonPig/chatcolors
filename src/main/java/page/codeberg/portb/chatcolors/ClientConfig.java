/*
 * Copyright (c) PORTB 2024
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package page.codeberg.portb.chatcolors;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
	public static final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

	public static final ModConfigSpec SPEC;

	public static final ModConfigSpec.BooleanValue enableExtendedSyntax;
	public static final ModConfigSpec.BooleanValue enableHexcodes;
	public static final ModConfigSpec.BooleanValue enableMarkdown;

	static {
		enableExtendedSyntax = builder.comment("Enable/disable using extended syntax, such as §[blue][u]hello")
				.define("extended_syntax", true);
		enableHexcodes = builder.comment("Enable/disable using hex codes. If disabled, all custom hex codes will be hidden and ignored.")
				.define("hex_codes", true);
		enableMarkdown = builder.comment("Enable/disable markdown formatting, such as *italic* or **bold**.")
				.define("markdown", true);

		SPEC = builder.build();
	}
}
