package com.destroystokyo.paper.console;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.bukkit.craftbukkit.v1_12_R1.command.ConsoleCommandCompleter;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public final class PaperConsole extends SimpleTerminalConsole {

    private final DedicatedServer server;

    public PaperConsole(DedicatedServer server) {
        this.server = server;
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder
            .appName("Paper")
            .completer(new ConsoleCommandCompleter(this.server))
        );
    }

    @Override
    protected boolean isRunning() {
        return !this.server.isServerStopped() && this.server.isServerRunning();
    }

    @Override
    protected void runCommand(String command) {
        this.server.addPendingCommand(command, this.server);
    }

    @Override
    protected void shutdown() {
        this.server.initiateShutdown();
    }

}
