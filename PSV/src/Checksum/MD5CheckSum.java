package Checksum;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Scanner;

public class MD5CheckSum {
	FileInputStream fis;

	public void start() {
		System.out.println(" You are in MD5Check Sum ");
		run();
	}

	private void run() {
		Scanner in = new Scanner(System.in);
		String input;

		System.out.println("First Time: Press any key to continue");
		input = in.nextLine();
		callCheckSum();

		System.out.println("\nSecond Time: Modify the source file and press any key to continue");
		input = in.nextLine();
		callCheckSum();

	}

	void callCheckSum() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1"); 
			byte[] dataBytes = new byte[1024];
			fis = new FileInputStream("C:\\Users\\user\\git\\PSV\\PSV\\src\\TestCheckSum");

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			byte[] mdbytes = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			System.out.println("Digest(in hex format):: " + sb.toString());
		} catch (Exception e) {
			System.out.println("Here is the exception : " + e);
		}
		///
	}
}
