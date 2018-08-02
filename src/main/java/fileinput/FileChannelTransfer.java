package fileinput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTransfer {

	public static void main(String[] args) throws Exception {

		try {
			File file = new File("D:\\test\\log\\TestWeb.log.2018-06-28.log");
			if (!file.exists()) {
				throw new IOException("文件不存在");
			}
			// RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt",
			// "rw");
			RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");

			FileChannel fromChannel = fromFile.getChannel();

			RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");

			FileChannel toChannel = toFile.getChannel();

			long position = 0;

			long count = fromChannel.size();

			toChannel.transferFrom(fromChannel, count, position);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
