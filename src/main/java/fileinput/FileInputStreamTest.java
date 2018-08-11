package fileinput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileInputStreamTest {
	public static void main(String[] args) throws Exception {

		try {
			File file = new File("fromFile.txt");
			if (!file.exists()) {
				throw new IOException("文件不存在");
			}
			FileInputStream fis = new FileInputStream(file);
			
			long start = System.currentTimeMillis();
			byte byteData;
			while ((byteData = (byte) fis.read()) != -1) {
				System.out.print((char) byteData);
			}
			long end = System.currentTimeMillis();
			long diff = end - start;
			System.out.println("读取所用时间:" + diff);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
