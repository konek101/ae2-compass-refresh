package com.konek101.compassrefresh.mixin;

import appeng.server.services.compass.ServerCompassService;
import appeng.util.InteractionUtil;
import com.konek101.compassrefresh.config.CompassRefreshConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(targets = "appeng.items.misc.MeteoriteCompassItem")
public class MeteoriteCompassItemMixin {
    
    @Unique
    private static final Map<UUID, Long> compassRefresh$cooldowns = new HashMap<>();

    @Inject(method = "use", at = @At("HEAD"), cancellable = true, remap = false)
    private void onUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        // Only handle shift + right-click on server side
        if (level.isClientSide || !InteractionUtil.isInAlternateUseMode(player)) {
            return;
        }

        ItemStack stack = player.getItemInHand(hand);
        
        // Check cooldown
        UUID playerId = player.getUUID();
        long currentTime = level.getGameTime();
        long cooldownTicks = CompassRefreshConfig.COOLDOWN_TICKS.get();
        
        if (compassRefresh$cooldowns.containsKey(playerId)) {
            long lastUse = compassRefresh$cooldowns.get(playerId);
            if (currentTime - lastUse < cooldownTicks) {
                // Still on cooldown
                return;
            }
        }
        
        // Update cooldown
        compassRefresh$cooldowns.put(playerId, currentTime);
        
        // Perform rescan
        int scannedChunks = compassRefresh$rescanChunks((ServerLevel) level, player);
        
        // Send chat feedback
        if (CompassRefreshConfig.SHOW_CHAT_FEEDBACK.get() && scannedChunks > 0) {
            player.sendSystemMessage(
                Component.translatable("message.compassrefresh.rescanned", scannedChunks)
                    .withStyle(ChatFormatting.GREEN)
            );
        }
        
        // Cancel the default behavior and return success
        cir.setReturnValue(InteractionResultHolder.success(stack));
    }

    @Unique
    private int compassRefresh$rescanChunks(ServerLevel level, Player player) {
        int radius = CompassRefreshConfig.SCAN_RADIUS.get();
        boolean onlyLoaded = CompassRefreshConfig.ONLY_LOADED_CHUNKS.get();
        
        int playerChunkX = player.chunkPosition().x;
        int playerChunkZ = player.chunkPosition().z;
        
        int scannedCount = 0;
        
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int chunkX = playerChunkX + dx;
                int chunkZ = playerChunkZ + dz;
                
                ChunkAccess chunk;
                if (onlyLoaded) {
                    chunk = level.getChunkSource().getChunk(chunkX, chunkZ, false);
                } else {
                    chunk = level.getChunk(chunkX, chunkZ);
                }
                
                if (chunk != null) {
                    ServerCompassService.updateArea(level, chunk);
                    scannedCount++;
                }
            }
        }
        
        return scannedCount;
    }

    @Inject(method = "appendHoverText", at = @At("TAIL"), remap = false)
    private void onAppendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
        tooltipComponents.add(
            Component.translatable("tooltip.compassrefresh.shift_use")
                .withStyle(ChatFormatting.GRAY)
        );
    }
}
