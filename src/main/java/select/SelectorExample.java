package select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorExample {

	public static void main(String[] args) throws IOException {
		// Get the selector
		Selector selector = Selector.open();
		System.out.println("Selector is open for making connection: " + selector.isOpen());
		// Get the server socket channel and register using selector
		ServerSocketChannel SS = ServerSocketChannel.open();
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8080);
		SS.bind(hostAddress);
		SS.configureBlocking(false);
		int ops = SS.validOps();
		SelectionKey selectKy = SS.register(selector, ops, null);
		for (;;) {
			System.out.println("Waiting for the select operation...");
			int noOfKeys = selector.select();
			System.out.println("The Number of selected keys are: " + noOfKeys);
			Set selectedKeys = selector.selectedKeys();
			Iterator itr = selectedKeys.iterator();
			while (itr.hasNext()) {
				SelectionKey ky = (SelectionKey) itr.next();
				if (ky.isAcceptable()) {
					// The new client connection is accepted
					SocketChannel client = SS.accept();
					client.configureBlocking(false);
					// The new connection is added to a selector
					client.register(selector, SelectionKey.OP_READ);
					System.out.println("The new connection is accepted from the client: " + client);
				} else if (ky.isReadable()) {
//					System.out.println("Start read method");
					SocketChannel client = (SocketChannel) ky.channel();
//					ThreadPool.execute(new RequestHandler(client, selector));
					// TODO keep-alive
//					ky.interestOps(ky.interestOps() & ~SelectionKey.OP_READ);// 目前一个请求对应一个socket连接，没有实现keep-alive
					// System.out.println("End read method");
					client.register(selector, SelectionKey.OP_WRITE, "");
				} else if (ky.isWritable()) {
					System.out.println("返回成功");
					SocketChannel client = (SocketChannel) ky.channel();
					String response = (String) ky.attachment();
					ByteBuffer byteBuffer = ByteBuffer.wrap(response.getBytes());
					if (byteBuffer.hasRemaining()) {
						client.write(byteBuffer);
					}
					if (!byteBuffer.hasRemaining()) {
						ky.cancel();
						client.close();
					}
				}
				itr.remove();
			} // end of while loop
		} // end of for loop
	}
}