package com.github.puzzle.paradox.api.events.packet;

import com.github.puzzle.game.commands.CommandSource;
import com.github.puzzle.paradox.api.player.ParadoxPlayer;
import com.mojang.brigadier.Command;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class ChatEvents extends Event {
    private final ParadoxPlayer player;

    protected ChatEvents(ParadoxPlayer player) {
        this.player = player;
    }


    public static class OnChatReceive extends ChatEvents implements ICancellableEvent {

        String message;

        public OnChatReceive(String message, ParadoxPlayer player) {
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

        public OnCommandReceive(String message, ParadoxPlayer player) {
            super(player);
            this.message = message;
        }

        String getMessage(){
            return message;
        }

    }

    public static class OnCommandExecute extends ChatEvents {

        Command<CommandSource> command;

        public OnCommandExecute(Command<CommandSource> command, ParadoxPlayer player) {
            super(player);
            this.command = command;
        }

        Command<CommandSource> getCommand(){
            return command;
        }
    }
}
