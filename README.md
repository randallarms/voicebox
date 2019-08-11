# VoiceBox

Simple custom chat plugin for Bukkit on Minecraft (MCv1.11.2).

VoiceBox features several chat modes reminiscent of popular MMOs' own stylings (say, whisper, yell, respond).

This plugin does differ, however, in that players must often be within physical range of the speaker to hear the chat (with the exception of global radio chat, aka 'broadcast' chat). While whispers must go out to close players, yelling/shouting can reach the ears of players further away, and say/respond lay somewhere in between.

commands: 

     vb:
        description: Info for the VoiceBox chat plugin.
        usage: /<command> {motd} {msg}
        aliases: [voicebox]
     clearChat:
        description: Clears chat.
        usage: /<command>
        aliases: [clearchat]
     re:
        description: Reply publicly to a close (< 20m away) player.
        usage: /<command> <name> <msg>
        aliases: [reply, respond]
     w:
        description: Whisper privately to a nearby (< 5m away) player.
        usage: /<command> <name> <msg>
        aliases: [whisper, tell]
     y:
        description: Yell loudly to all who can hear you (< 126m away).
        usage: /<command> <msg>
        aliases: [yell, shout]
     radio:
        description: Commands for radios.
        usage: /<command>
     ch:
        description: Set your radio's frequency.
        usage: /<command> <frequency>
        aliases: [channel, frequency, freq, tune]
     grumble:
        description: Grumble jibberish to close (< 20m away) players.
        usage: /<command>
     censor:
        description: Adjust the censor of blacklisted chat phrases.
        usage: /<command> <add/remove> <phrase>
     quote:
        description: Get a random quote, or add a quote.
        usage: /<command> {author} {quote}
        aliases: [quotes]
     joinMsg:
        description: Enable/disable player join messages.
        usage: /<command> <on/off>
        aliases: [joinmsg, joinMessage, joinmessage]

VoiceBox will likely interfere with plugins and features like EssentialsChat or typical Vault-based chat plugins. VoiceBox has no dependencies and serves simply to allow realistic conversation styles without all of the extra mess.

Any forks, branches, and pull requests are welcome! Please feel free to voice criticism to better the project, as well.

Got a problem? Bug, glitch, complaint? Visit the Issues page and let me know, please.
