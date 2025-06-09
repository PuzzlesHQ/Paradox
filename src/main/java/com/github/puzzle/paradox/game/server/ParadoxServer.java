package com.github.puzzle.paradox.game.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParadoxServer {

    public ChannelFuture channelfuture;
    public EventLoopGroup eventloopgroup;
    public EventLoopGroup eventloopgroup1;
    protected int port;
    public int getPort() {
        return this.port;
    }

    protected final static Logger LOGGER = LoggerFactory.getLogger("Cosmic Reach | Server");

}
