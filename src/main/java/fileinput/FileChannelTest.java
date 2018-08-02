package fileinput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
	public static void main(String[] args) throws Exception {

		try {
			File file = new File("D:\\test\\log\\TestWeb.log.2018-06-28.log");
			if (!file.exists()) {
				throw new IOException("文件不存在");
			}
//			RandomAccessFile aFile = new RandomAccessFile(file, "rw");
			FileInputStream aFile = new FileInputStream(file);
			long start = System.currentTimeMillis();

			FileChannel inChannel = aFile.getChannel();

			ByteBuffer buf = ByteBuffer.allocate(1024);

			int bytesRead = inChannel.read(buf);
			while (bytesRead != -1) {
				System.out.println("Read " + bytesRead);
				buf.flip();

				while (buf.hasRemaining()) {
					System.out.print((char) buf.get());
				}

				buf.clear();
				bytesRead = inChannel.read(buf);

			}

			long end = System.currentTimeMillis();
			long diff = end - start;
			System.out.println("读取所用时间:" + diff);

			aFile.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
