package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	private  String firstName = "suhas";
	private  String lastName = "murthy";
	private  String userName = "suhas";
	private  String password = "password";
	
	private  String credentialURL = "google.com";
	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Dell\\Downloads\\chromedriver-win64\\chromedriver.exe");
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getUnauthorizedHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	@Test
	public void newUserAccessTest() {
		WebDriverWait wait = new WebDriverWait(driver, 30);

		// Navigate to the signup page and fill in the signup form
		driver.get("http://localhost:" + this.port + "/signup");
		driver.findElement(By.id("inputFirstName")).sendKeys(firstName);
		driver.findElement(By.id("inputLastName")).sendKeys(lastName);
		driver.findElement(By.id("inputUsername")).sendKeys(userName);
		driver.findElement(By.id("inputPassword")).sendKeys(password);
		driver.findElement(By.id("buttonSignUp")).click();

		// Log in
		login();

		// Check if the title of the page is "Home" after successful login
		Assertions.assertEquals("Home", driver.getTitle());

		// Logout and check if the title is "Login"
		driver.findElement(By.id("logout")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));
		Assertions.assertEquals("Login", driver.getTitle());

		// Try accessing the home page after logout and check if the title is "Login"
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNoteManagement() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// Log in
		login();

		// Create a new note
		createNote(wait,jse);

		// Check if the note is created
		Assertions.assertTrue(isNoteCreated(jse));

		// Update the note
		updateNote("New Note Title", wait,jse);

		// Check if the note is updated
		Assertions.assertTrue(isNoteUpdated("New Note Title",jse));

		// Delete the note
		deleteNote(wait, jse);
	}

	@Test
	public void testCredentialManagement() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// Log in
		login();

		// Create a new credential
		createCredential(wait,jse);

		// Check if the credential is created
		Assertions.assertTrue(isCredentialCreated(jse));

		// Update the credential
		updateCredential("NewUsername",wait,jse);

		// Check if the credential is updated
		Assertions.assertTrue(isCredentialUpdated("NewUsername",jse));

		// Delete the credential
		deleteCredential(wait,jse);
	}

	private void login() {
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());
	}

	private void createNote(WebDriverWait wait,JavascriptExecutor jse) {
		navigateToNotesTab(jse);
		WebElement newNote = driver.findElement(By.id("newnote"));
		wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys("Note Title");
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.sendKeys("Note Description");
		WebElement saveChanges = driver.findElement(By.id("save-changes"));
		saveChanges.click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	private boolean isNoteCreated(JavascriptExecutor jse) {
		driver.get("http://localhost:" + this.port + "/home");
		navigateToNotesTab(jse);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
		for (WebElement element : notesList) {
			if (element.getAttribute("innerHTML").equals("Note Title")) {
				return true;
			}
		}
		return false;
	}

	private void updateNote(String newNoteTitle, WebDriverWait wait, JavascriptExecutor jse) {
		navigateToNotesTab(jse);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement editElement = null;
		for (WebElement element : notesList) {
			editElement = element.findElement(By.name("edit"));
			if (editElement != null) {
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		wait.until(ExpectedConditions.elementToBeClickable(noteTitle));
		noteTitle.clear();
		noteTitle.sendKeys(newNoteTitle);
		WebElement saveChanges = driver.findElement(By.id("save-changes"));
		saveChanges.click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	private boolean isNoteUpdated(String newNoteTitle,JavascriptExecutor jse) {
		driver.get("http://localhost:" + this.port + "/home");
		navigateToNotesTab(jse);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
		for (WebElement element : notesList) {
			if (element.getAttribute("innerHTML").equals(newNoteTitle)) {
				return true;
			}
		}
		return false;
	}

	private void deleteNote(WebDriverWait wait,JavascriptExecutor jse) {
		navigateToNotesTab(jse);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for (WebElement element : notesList) {
			deleteElement = element.findElement(By.name("delete"));
			if (deleteElement != null) {
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	private void navigateToNotesTab(JavascriptExecutor jse) {
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
	}
	private void createCredential(WebDriverWait wait,JavascriptExecutor jse) {
		navigateToCredentialsTab(jse);
		WebElement newCredential = driver.findElement(By.id("newCredential"));
		wait.until(ExpectedConditions.elementToBeClickable(newCredential)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credentialURL);
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys(userName);
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys(password);
		WebElement submit = driver.findElement(By.id("save-credential"));
		submit.click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	private boolean isCredentialCreated(JavascriptExecutor jse) {
		driver.get("http://localhost:" + this.port + "/home");
		navigateToCredentialsTab(jse);
		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialsTable.findElements(By.tagName("td"));
		for (WebElement element : credentialsList) {
			if (element.getAttribute("innerHTML").equals(userName)) {
				return true;
			}
		}
		return false;
	}

	private void updateCredential(String newUsername,WebDriverWait wait, JavascriptExecutor jse) {
		navigateToCredentialsTab(jse);
		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialsTable.findElements(By.tagName("td"));
		WebElement editElement = null;
		for (WebElement element : credentialsList) {
			editElement = element.findElement(By.name("editCredential"));
			if (editElement != null) {
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		wait.until(ExpectedConditions.elementToBeClickable(credentialUsername));
		credentialUsername.clear();
		credentialUsername.sendKeys(newUsername);
		WebElement saveChanges = driver.findElement(By.id("save-credential"));
		saveChanges.click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	private boolean isCredentialUpdated(String newUsername,JavascriptExecutor jse) {
		driver.get("http://localhost:" + this.port + "/home");
		navigateToCredentialsTab(jse);
		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialsTable.findElements(By.tagName("td"));
		for (WebElement element : credentialsList) {
			if (element.getAttribute("innerHTML").equals(newUsername)) {
				return true;
			}
		}
		return false;
	}

	private void deleteCredential(WebDriverWait wait, JavascriptExecutor jse) {
		navigateToCredentialsTab(jse);
		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialsTable.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for (WebElement element : credentialsList) {
			deleteElement = element.findElement(By.name("delete"));
			if (deleteElement != null) {
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Result", driver.getTitle());
	}

	private void navigateToCredentialsTab( JavascriptExecutor jse) {
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credentialsTab);
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}
