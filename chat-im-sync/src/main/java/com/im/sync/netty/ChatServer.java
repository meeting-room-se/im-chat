package com.im.sync.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@Slf4j
public class ChatServer{

	
	@Value("${netty.port}")
	private Integer port;

	// 避免使用默认线程数参数
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	
	
	public void start() throws Exception {
		log.info("服务端已启动，正在监听用户的请求......");

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(new WsChannelInitializer());

			b.bind(new InetSocketAddress(port)).sync();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}finally {
			// 释放资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}


	public void shutDown() throws Exception {
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
	}


}
