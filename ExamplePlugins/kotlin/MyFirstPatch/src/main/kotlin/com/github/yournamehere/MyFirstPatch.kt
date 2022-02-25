package com.github.jaidadonut

import android.content.Context
import com.aliucord.Logger
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.before
import com.discord.models.domain.ModelMessageDelete
import com.discord.stores.StoreStream.getMessages
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage
import com.discord.widgets.chat.list.entries.ChatListEntry
import com.discord.widgets.chat.list.entries.MessageEntry


// Aliucord Plugin annotation. Must be present on the main class of your plugin
@AliucordPlugin(requiresRestart = false /* Whether your plugin requires a restart after being installed/updated */)
// Plugin class. Must extend Plugin and override start and stop
// Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/1_introduction.md#basic-plugin-structure
class KarebuBeGone : Plugin() {
    private var logger = Logger("KarebuBeGone")
    override fun start(context: Context) {
        val id_karebu = "916480120494112798"
        logger.debug("patching message onConfigure")
        // Patch that adds an embed with message statistics to each message
        // Patched method is WidgetChatListAdapterItemMessage.onConfigure(int type, ChatListEntry entry)
        patcher.before<WidgetChatListAdapterItemMessage>(
            "onConfigure",
            Int::class.java, // int type
            ChatListEntry::class.java // ChatListEntry entry
        )
        { param ->
            var msg = param.args[1] as MessageEntry
            if (msg.author.userId.toString() == id_karebu) //lol
            {
                getMessages().handleMessageDelete(
                    ModelMessageDelete(
                        msg.message.channelId,
                        msg.message.id
                    )
                )
            }
        }
        // remove dm channel :)))
//        patcher.before<WidgetChannelsList>("configureUI", WidgetChannelListModel::class.java) {
//            // code based off of zt/dmcategories
//            val model = it.args[0] as WidgetChannelListModel
//            if (model.selectedGuild != null) return@before // no guild
//            val privateChannels = model.items.filterIsInstance<ChannelListItemPrivate>()
//            val karebuChannelId: String
//            // @zt you will tell me how to get a user's id from `ChannelListItemPrivate` immediately
////            val channels = privateChannels.filter { channel ->
////
////            }
//        }
    }

    override fun stop(context: Context) {
        // Remove all patches
        patcher.unpatchAll()
    }
}
