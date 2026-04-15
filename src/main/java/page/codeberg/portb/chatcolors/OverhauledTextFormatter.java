/*
 * Copyright (c) PORTB 2024
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package page.codeberg.portb.chatcolors;

import page.codeberg.portb.chatcolors.common.InvalidHexCodeException;
import page.codeberg.portb.chatcolors.common.UniCharacterConsumer;
import page.codeberg.portb.chatcolors.common.UniTextStyle;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class OverhauledTextFormatter {
	static private final Pattern OLD_FORMAT_SYNTAX_PATTERN = Pattern.compile("\\[([^]]+)]");

	// Character 65533 is a 'replacement character' for an invalid unicode character
	// https://www.fileformat.info/info/unicode/char/fffd/index.htm
	private static final char UNICODE_REPLACEMENT_CHAR = 65533;

	public static boolean format(String string, int startIndex, UniTextStyle initialStyle, UniTextStyle resetStyle, UniCharacterConsumer consumer) {
		final int strLen = string.length();
		int starCount = 0;
		boolean escapeNextFormattingChar = false;

		for (int charIndex = startIndex; charIndex < strLen; charIndex++) {
			char prevChar = (charIndex == startIndex) ? 0 : string.charAt(charIndex - 1);
			char currentChar = string.charAt(charIndex);
			char nextChar = (charIndex == strLen - 1) ? 0 : string.charAt(charIndex + 1);

			if (!escapeNextFormattingChar) {
				//Process markdown syntax
				if (isMarkdownEnabled()) {
					//Detect escaped formatting characters
					if (currentChar == '\\' && (nextChar == '*' || nextChar == '~' || nextChar == '_')) {
						escapeNextFormattingChar = true;
						//Skip this current \ so we don't feed it to the consumer and show it
						continue;
					}

					//Bold, italic
					if (currentChar == '*') {
						starCount++;

						if (nextChar != '*') {
							switch (starCount) {
								case 1:
									initialStyle.toggleItalic();
									break;
								case 2:
									initialStyle.toggleBold();
									break;
								case 3:
									initialStyle.toggleItalic();
									initialStyle.toggleBold();
									break;
								default:
									//what?
							}

							starCount = 0;
						}

						//Don't feed the stars to the consumer
						continue;
					}
					//Strikeout
					else if (currentChar == '~' && nextChar == '~') {
						initialStyle.toggleStrikethrough();
						//Skip the next char
						charIndex++;

						//Don't feed the tildes to the consumer
						continue;
					}
					//Underline
					else if (currentChar == '_' && nextChar == '_') {
						initialStyle.toggleUnderlined();
						//Skip the next char
						charIndex++;

						//Don't feed the underscores to the consumer
						continue;
					}
				}

				if (isHexCodesEnabled()) {
					//Detect escaped formatting characters
					if (currentChar == '\\' && nextChar == '[') {
						escapeNextFormattingChar = true;

						//Don't feed it to the consumer
						continue;
					}
					//Detect hex color codes
					else if (currentChar == '[') {
						//check that there is enough room, and that it is a valid hex color
						if (charIndex + 8 < strLen &&
								string.substring(charIndex,
										charIndex + 9
								).matches("\\[#[a-fA-F0-9]{6}]")) //matches hex codes formatted like [#aabbcc]
						{
							try {
								initialStyle.setColor(string.substring(charIndex + 1, charIndex + 8));

								//skip the color code
								charIndex += 8;

								continue;
							} catch (InvalidHexCodeException ignored) {
								//Expected
							}
						}
					}
				}

			} else {
				escapeNextFormattingChar = false;
			}

			//Vanilla formatting
			if (currentChar == 167) {
				boolean processedFormatting = false;

				//Old formatting
				if (isOldSyntaxEnabled() && nextChar == '[') {
					var allMatches = new ArrayList<MatchResult>();
					var matcher = OLD_FORMAT_SYNTAX_PATTERN.matcher(string.substring(charIndex + 1));

					//Match all chained codes, e.g. [red][bold][underline]
					while (matcher.find())
						allMatches.add(matcher.toMatchResult());

					if (!allMatches.isEmpty()) {
						for (var match : allMatches) {
                            var colorName = match.group(1).toLowerCase();

							if (HumanReadableColorNames.isValidCode(colorName)) {
                                var code = HumanReadableColorNames.byCode(colorName);

								//Check if the code is the reset code, it is handled differently
								if (code == 'r')
									initialStyle = resetStyle;
								else
									initialStyle.applyLegacyFormat(code);
							} else {
								if (!feedSubstringWithOffset(initialStyle, consumer, match.group(0), charIndex))
									return false;
							}

							charIndex += match.end() - match.start();
						}

						processedFormatting = true;
					}
				}

				//Don't do vanilla stuff if we already found some modded formatting
				if (!processedFormatting) {
					if (charIndex + 1 >= strLen)
						break;

					//Check if the code is the reset code, it is handled differently
					if (nextChar == 'r')
						initialStyle = resetStyle;
					else
						initialStyle.applyLegacyFormat(nextChar);

					//Don't show the formatting code in output
					charIndex++;
				}
			}
			//Vanilla stuff
			else if (Character.isHighSurrogate(currentChar)) {
				if (charIndex + 1 >= strLen) {
					if (!consumer.accept(charIndex, initialStyle, UNICODE_REPLACEMENT_CHAR))
						return false;

					break;
				}

				if (Character.isLowSurrogate(nextChar)) {
					if (!consumer.accept(charIndex, initialStyle, Character.toCodePoint(currentChar, nextChar)))
						return false;

					++charIndex;
				} else if (!consumer.accept(charIndex, initialStyle, UNICODE_REPLACEMENT_CHAR)) {
					return false;
				}
			} else if (!feedChar(initialStyle, consumer, charIndex, currentChar)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Feeds a substring into a character consumer, using the offset as the position of the substring in the original
	 * string so the consumer can see the characters in the correct index.
	 *
	 * @param style    The style
	 * @param consumer The consumer to feed into
	 * @param string   The substring to iterate over
	 * @param offset   An offset which accounts for the substring's position in the superstring
	 */
	private static boolean feedSubstringWithOffset(UniTextStyle style, UniCharacterConsumer consumer, String string, int offset) {
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);

			if (!(Character.isSurrogate(c) ?
					consumer.accept(offset + i, style, 65533)
					: consumer.accept(offset + i, style, c)
			)) {
				return false;
			}
		}

		return true;
	}

	private static boolean feedChar(UniTextStyle style, UniCharacterConsumer consumer, int index, char character) {
		return Character.isSurrogate(character) ?
				consumer.accept(index, style, UNICODE_REPLACEMENT_CHAR)
				: consumer.accept(index, style, character);
	}

	private static boolean isMarkdownEnabled() {
		try {
			return ClientConfig.enableMarkdown.get();
		} catch (IllegalStateException shutUpForge) {
			return true;
		}
	}

	private static boolean isHexCodesEnabled() {
		try {
			return ClientConfig.enableHexcodes.get();
		} catch (IllegalStateException shutUpForge) {
			return true;
		}
	}

	private static boolean isOldSyntaxEnabled() {
		try {
			return ClientConfig.enableExtendedSyntax.get();
		} catch (IllegalStateException shutUpForge) {
			return true;
		}
	}
}
