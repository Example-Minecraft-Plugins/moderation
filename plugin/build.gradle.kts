import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "me.davipccunha.tests.moderation"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":api"))
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveFileName.set("moderation.jar")

    destinationDirectory.set(file("D:\\Local Minecraft Server\\plugins"))
}

bukkit {
    name = "moderation"
    prefix = "Moderation" // As shown in console
    apiVersion = "1.8"
    version = "${project.version}"
    main = "me.davipccunha.tests.moderation.ModerationPlugin"
    depend = listOf("bukkit-utils")
    description = "Plugin that allows muting and facilitates banning."
    author = "Davi C"

    commands {
        register("expulsar") {
            aliases = listOf("kick")
            description = "Expulsa um jogador"
        }

        register("banir") {
            aliases = listOf("ban")
            description = "Bane um jogador"
        }

        register("desbanir") {
            aliases = listOf("unban", "pardon")
            description = "Desbane um jogador"
        }

        register("silenciar") {
            aliases = listOf("calar", "mute", "mutar")
            description = "Proíbe que um jogador envie mensagens em chats públicos"
        }

        register("dessilenciar") {
            aliases = listOf("unmute", "desmutar", "descalar")
            description = "Permite que um jogador envie mensagens em chats públicos"
        }

        register("checarpunicao") {
            aliases = listOf("punicoes", "verificarpunicao", "checarpunicoes")
            description = "Verifica as punições ativas de um jogador"
        }
    }
}

