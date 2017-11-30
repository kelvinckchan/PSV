package app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class CheckSum {
	FileInputStream fis;

	public static void main(String[] args) {
		CheckSum app = new CheckSum();
		app.callCheckSum();
	}

	void callCheckSum() {
		try {
			generateCheckSun("MD5", new File("TestCheckSum"));
			System.out.println("Generated");
			System.out.println("CheckSum> " + verifyCheckSum("MD5", new File("TestCheckSum")));
		} catch (Exception e) {
			System.out.println("Here is the exception : " + e);
		}
	}

	public boolean verifyCheckSum(String mode, File file) throws IOException {
		String format = "";
		if (mode.equals("MD5")) {
			format = ".md5";
		} else if (mode.equals("SHA-1")) {
			format = ".sha1";
		}
		String existCheckSumPath = FileUtil.changeExtension(file.getAbsolutePath(), format);
		String newCheckSum = new HexBinaryAdapter().marshal(getCheckSum(mode, Files.readAllBytes(file.toPath())));
		String existCheckSum = new HexBinaryAdapter().marshal(Files.readAllBytes(Paths.get(existCheckSumPath)));
		return newCheckSum.equals(existCheckSum);
	}

	public static void generateCheckSun(String mode, File file) throws IOException {
		String format = "";
		if (mode.equals("MD5")) {
			format = ".md5";
		} else if (mode.equals("SHA-1")) {
			format = ".sha1";
		}
		String checkSumPath = FileUtil.changeExtension(file.getAbsolutePath(), format);
		Files.write(Paths.get(checkSumPath), getCheckSum(mode, Files.readAllBytes(file.toPath())));
	}

	public static byte[] getCheckSum(String mode, byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance(mode);
			md.update(data);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getCheckSum(String mode, File file) {
		return getCheckSum(mode, FileUtil.importByteArrayFromFile(file));
	}

}
