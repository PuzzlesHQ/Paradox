package com.github.puzzle.paradox.api.impl.events.packet;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.api.interfaces.player.IParadoxPlayer;
import com.mojang.brigadier.Command;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class ChatEvents extends Event {
    private final IParadoxPlayer player;

    protected ChatEvents(IParadoxPlayer player) {
        this.player = player;
    }


    public static class OnChatReceive extends ChatEvents implements ICancellableEvent {

        String message;

        public OnChatReceive(String message, IParadoxPlayer player) {
            super(player);
            this.message = message;
        }

        String getMessage(){
            return message;
        }

        void setMessage(String message){
            this.message = message;
        }
    }

    public static class OnCommandReceive extends ChatEvents {

        String message;

        public OnCommandReceive(String message, IParadoxPlayer player) {
            super(player);
            this.message = message;
        }

        String getMessage(){
            return message;
        }

    }

    public static class OnCommandExecute extends ChatEvents {

        Command<CommandSource> command;

        public OnCommandExecute(Command<CommandSource> command, IParadoxPlayer player) {
            super(player);
            this.command = command;
        }

        Command<CommandSource> getCommand(){
            return command;
        }
    }
}
