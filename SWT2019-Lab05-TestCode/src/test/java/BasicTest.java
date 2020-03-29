import org.junit.Test;
import org.openqa.selenium.By;

import static junit.framework.TestCase.assertEquals;

public class BasicTest extends TestHelper {

    private String username = "admin";
    private String password = "admin";

    @Test
    public void titleExistsTest(){
        String expectedTitle = "ST Online Store";
        String actualTitle = driver.getTitle();

        assertEquals(expectedTitle, actualTitle);
    }


    /*
    In class Exercise

    Fill in loginLogoutTest() and login mehtod in TestHelper, so that the test passes correctly.

     */
     @Test
    public void loginLogoutTest(){

        login(username, password);

        String expected = "Products";
        String actual = driver.findElement(By.id("Products")).getText();

        assertEquals(expected,actual);
        // assert that correct page appeared
        // WebElement adminHeader = driver.findElement...
        // ...

        logout();
    }

    /*
    In class Exercise

     Write a test case, where you make sure, that one can’t log in with a false password

     */
     @Test
    public void loginFalsePassword() {
        String wrongPassword = "123";
        login(username,wrongPassword);
        String expected = "Invalid user/password combination";
        String actual = driver.findElement(By.id("notice")).getText();
        assertEquals(expected,actual);
    }

}
