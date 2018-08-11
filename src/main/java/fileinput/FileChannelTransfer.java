package fileinput;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
			
			long start = System.currentTimeMillis();
			
			@SuppressWarnings("resource")
			RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");

			FileChannel fromChannel = fromFile.getChannel();

			@SuppressWarnings("resource")
			RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");

			FileChannel toChannel = toFile.getChannel();

			long position = 0;

			long count = fromChannel.size();

			fromChannel.transferTo(position, count, toChannel);

			
			long end = System.currentTimeMillis();
			long diff = end - start;
			System.out.println("读取所用时间:" + diff);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
