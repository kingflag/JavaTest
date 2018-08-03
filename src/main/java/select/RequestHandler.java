package select;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;


public class RequestHandler implements Runnable {

	private SocketChannel channel;
	private Selector selector;

	public RequestHandler(SocketChannel channel, Selector selector) {
		this.channel = channel;
		this.selector = selector;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		HttpServletRequestWrapper request = null;
		HttpServletResponseWrapper response = null;
		
		try {
	        //assume size of (requestline + headers) <= 1024kb
	        ByteBuffer buffer = ByteBuffer.allocate(1024);
	        channel.read(buffer);
	        buffer.flip();
	        int remaining = buffer.remaining();
	        if(remaining == 0){
//	            return null;
	        }
	        byte[] bytes = new byte[remaining];
	        buffer.get(bytes);
	        int position1 = BytesUtil.indexOf(bytes, "\r\n");
	        int position2 = BytesUtil.indexOf(bytes, "\r\n\r\n");
	        if(position1 == -1 || position2 == -1){
	            throw new Exception("Illegal request format");
	        }

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
