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
				throw new IOException("�ļ�������");
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			long start = System.currentTimeMillis();
			byte byteData;
			while ((byteData = (byte) bis.read()) != -1) {
				System.out.println((char) byteData);
			}
			long end = System.currentTimeMillis();
			long diff = end - start;

			System.out.println("��ȡʱ�䣺" + diff);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
