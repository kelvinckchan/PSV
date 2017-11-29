package Checksum;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Scanner;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class SHA1CheckSum {
	FileInputStream fis;

	public void start() {
		System.out.println(" You are in SHA1Check Sum ");
		run();
	}

	private void run() {
		Scanner in = new Scanner(System.in);
		String input;

		System.out.println("First Time: Press any key to continue");
		input = in.nextLine();
		callCheckSum();
///
		System.out.println("\nSecond Time: Modify the source file and press any key to continue");
		input = in.nextLine();
		callCheckSum();

	}

	private void callCheckSum() {

		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			fis = new FileInputStream("C:\\Users\\user\\git\\PSV\\PSV\\src\\TestCheckSum"); 
			InputStream input = fis;

			byte[] buffer = new byte[8192];
			int len = input.read(buffer);

			while (len != -1) {
				sha1.update(buffer, 0, len);
				len = input.read(buffer);
			}

			System.out.println(new HexBinaryAdapter().marshal(sha1.digest()));
		} catch (Exception e) {
			System.out.println("Here is the exception : " + e);
		}

	}
}
