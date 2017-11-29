package TestCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtil {
	public static byte[] importByteArrayFromFile(String path) {
		try {
			Path fileLocation = Paths.get(path);
			return Files.readAllBytes(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] importByteArrayFromFile(File file) {
		try {
			return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void exportByteArrayToFile(String path, byte[] data) {
		try {
			Path fileLocation = Paths.get(path);
			Files.write(fileLocation, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String changeExtension(String filePath, String format) {
		if (filePath.lastIndexOf(".") != -1) {
			return filePath.replace(filePath.substring(filePath.lastIndexOf(".")), format);
		}
		return filePath + format;
	}

}
