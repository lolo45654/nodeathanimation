package me.loloed.nodeathanimation;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import org.bukkit.entity.Player;

public class PacketListener extends SimplePacketListenerAbstract {
    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        Player player = (Player) event.getPlayer();
        Object buffer = event.getByteBuf();
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_STATUS) {
            WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus(event);
            if (packet.getStatus() == 3) event.setCancelled(true);
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event);
            if (packet.getEntityId() == player.getEntityId()) return;
            for (EntityData entityData : packet.getEntityMetadata()) {
                if (entityData.getIndex() != 9) continue;
                if (entityData.getType() != EntityDataTypes.FLOAT) continue;
                float value = (float) entityData.getValue();
                if (value > 0.0f) continue;
                entityData.setValue(1.0f);
                event.markForReEncode(true);
            }
        }
    }
}
