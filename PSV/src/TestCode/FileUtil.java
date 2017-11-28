package TestCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtil {
	public static byte[] importFileAsByteArray(String path) {
		try {
			Path fileLocation = Paths.get(path);
			return Files.readAllBytes(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void exportFileAsByteArray(String path, byte[] data) {
		try {
			Path fileLocation = Paths.get(path);
			Files.write(fileLocation, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
