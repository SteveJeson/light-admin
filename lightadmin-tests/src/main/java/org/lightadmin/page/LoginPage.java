package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.data.User;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.Assert.fail;

@Component
public class LoginPage extends BasePage<LoginPage> {

	@FindBy( id = "j_username" )
	private WebElement login;

	@FindBy( id = "j_password" )
	private WebElement password;

	@FindBy( id = "signIn" )
	private WebElement submitButton;

	@Autowired
	public LoginPage( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public LoginPage enterLogin( String login ) {
		clearAndType( this.login, login );
		return this;
	}

	public LoginPage enterPassword( String password ) {
		clearAndType( this.password, password );
		return this;
	}

	public void submit() {
		submitButton.submit();
	}

	public boolean isLoggedOut() {
		return isElementPresent( submitButton );
	}

	public DashboardPage loginAs( User user ) {
		return loginAs( user.getLogin(), user.getPassword() );
	}

	public DashboardPage loginAs( String login, String password ) {
		enterLogin( login ).enterPassword( password ).submit();

		return new DashboardPage( seleniumContext ).get();
	}

	public LoginPage loginAsExpectingError( User user ) {
		return loginAsExpectingError( user.getLogin(), user.getPassword() );
	}

	public LoginPage loginAsExpectingError( String login, String password ) {
		enterLogin( login ).enterPassword( password ).submit();

		return new LoginPage( seleniumContext ).get();
	}

	public String errorMessage() {
		return webDriver().findElement( By.className( "alert-message" ) ).getText();
	}

	@Override
	protected void load() {
		webDriver().get( baseUrl() + "/login" );
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			webDriver().findElement( By.xpath( "//div[@id='login']" ) );
		} catch ( NoSuchElementException e ) {
			fail();
		}
	}
}