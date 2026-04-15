package page.codeberg.portb.chatcolors.mixin;

import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StringUtil.class)
public class StringUtilMixin {

	@Inject(method = "isAllowedChatCharacter", at = @At("RETURN"), cancellable = true, remap = false)
	private static void isAllowedCharacter(char c, CallbackInfoReturnable<Boolean> returnInfo) {
		returnInfo.setReturnValue(c >= ' ' && c != 127);
	}
}
