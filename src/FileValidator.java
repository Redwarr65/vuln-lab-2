import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class FileValidator {
	private static final Pattern filenameRegex = Pattern.compile("^[a-zA-Z0-9\\\\/_\\-.:]+\\.txt$");
	public static FileValidationResult fileNameIsValid(Path filename) {
		filename = filename.normalize();
		if(!filename.toString().endsWith(".txt"))
				return new FileValidationResult(false, "Invalid Filetype");
		if (!filenameRegex.matcher(filename.toString()).matches())
				return new FileValidationResult(false, "Invalid Filename");

		//Validate Target Directory (assets)
		String currentDirectory = System.getProperty("user.dir");
		String assetsFolderPath = currentDirectory + File.separator + "assets";
		if (!filename.startsWith(assetsFolderPath))
				return new FileValidationResult(false, "Provided path outside of assets folder");

		// Check if file is too large
		try {
			if (isFileTooLarge(filename)) return new FileValidationResult(false, "File Too Large");
		} catch (IOException e) {
			return new FileValidationResult(false, e.getMessage());
		}

		return new FileValidationResult(true);
	}
	private static boolean isFileTooLarge(Path filename) throws IOException {
		long fileSize = filename.toFile().length();
		long maxFileSize = 1000000; // 1 MB
		return fileSize > maxFileSize;
	}
}
class FileValidationResult {
	boolean okay;
	String msg;
	public FileValidationResult(boolean okay) {
		this.okay = okay;
		this.msg = "Okay";
	}
	public FileValidationResult(boolean okay, String msg) {
		this.okay = okay;
		this.msg = msg;
	}
}