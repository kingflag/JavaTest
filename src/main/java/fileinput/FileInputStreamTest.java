package fileinput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileInputStreamTest {
	public static void main(String[] args) throws Exception {

		try {
			File file = new File("D:\\test\\log\\TestWeb.log.2018-06-28.log");
			if (!file.exists()) {
				throw new IOException("�ļ�������");
			}
			FileInputStream fis = new FileInputStream(file);

			long start = System.currentTimeMillis();
			byte byteData;
			while ((byteData = (byte) fis.read()) != -1) {
				System.out.println((char) byteData);
			}
			long end = System.currentTimeMillis();
			long diff = end - start;
			System.out.println("��ȡ����ʱ��:" + diff);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
