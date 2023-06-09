import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class Tests {
	RegexTest[] emails = new RegexTest[] {
			  new RegexTest("doug@arapahoe.edu", true),
			  new RegexTest("nina@arapahoe.edu", true),
			  new RegexTest("tracy@arapahoe.edu", true),
			  new RegexTest("TESTING!@#$%@arapahoe.edu", false),
			  new RegexTest("jodi@arapahoe.edu", true),
			  new RegexTest("kristi@arapahoe.edu", true),
			  new RegexTest("TestText@#$..edu", false),
			  new RegexTest("TestingAgain@@..comdf", false),
			  new RegexTest("jessie@arapahoe.edu", true),
			  new RegexTest("laura%@arapahoe.edu", false),
			  new RegexTest("tina@arapahoe.edu", true),
			  new RegexTest("todd@arapahoe.edu", true),
			  new RegexTest("douglas.lundin@student.arapahoe.edu", true),
			  new RegexTest("<script>test</script>", false),
			  new RegexTest("ed@arapahoe.edu", false),
	};
	@TestFactory
	Stream<DynamicTest> testCorrectEmails() {
		return Arrays.stream(emails).map(entry -> {
			return dynamicTest(entry.str + " is " + (entry.valid ? "Valid": "Invalid"), () -> {
				assertEquals(ValidateEmail.emailIsValid(entry.str), entry.valid);
			});
		});
	}
	RegexTest[] filenames = new RegexTest[] {
			  new RegexTest("assets/Email_addresses_20210205.txt", true),
			  new RegexTest("assets/localemails.txt", true),
			  new RegexTest("assets/../../.ssh/id_rsa.txt", false),
			  new RegexTest("all_emails.csv", false),
			  new RegexTest("../hidden.txt", false),
			  new RegexTest("subdir/document.txt", false),
			  new RegexTest("/etc/shadow", false),
			  new RegexTest("C:\\\\Windows\\\\System32\\\\config", false),
			  new RegexTest("/etc/shadow", false),
			  new RegexTest("~/.ssh/id_rsa.key", false),
			  new RegexTest("www.google.com", false),
			  new RegexTest("<script>alert('xxs')</script>", false),
	};
	@TestFactory
	Stream<DynamicTest> testCorrectFilenames() {
		return Arrays.stream(filenames).map(entry -> {
			String path = System.getProperty("user.dir");
			return dynamicTest(entry.str + " is " + (entry.valid ? "Valid": "Invalid"), () -> {
				Path filepath = Paths.get(path, entry.str);
				FileValidationResult res = FileValidator.fileNameIsValid(filepath);
				System.out.println(res.msg);
				assertEquals(res.okay, entry.valid);
			});
		});
	}
}

class RegexTest {
	String str;
	boolean valid;
	public RegexTest(String email, boolean valid) {
		this.str = email;
		this.valid = valid;
	}
}