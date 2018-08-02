package fileinput;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BufferInputStream {
	public static void main(String[] args) {
		try {
			File file = new File("D:\\test\\log\\TestWeb.log.2018-06-28.log");
			if (!file.exists()) {
				throw new IOException("文件不存在");
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			long start = System.currentTimeMillis();
			byte byteData;
			while ((byteData = (byte) bis.read()) != -1) {
				System.out.print((char) byteData);
			}
			long end = System.currentTimeMillis();
			long diff = end - start;

			System.out.println("读取所用时间:" + diff);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
