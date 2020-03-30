import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

public class AdminTest extends TestHelper {
    private String username = "admin";
    private String password = "admin";
    private String productTitle = "Meditations";
    private String productDescription = "Roman emperor and a stoic philosopher - Marcus Aurelius";
    private String productPrice = "15";
    private String productType = "Books";

    @Before
    public void adminSetup() {
        driver.get(baseUrlAdmin);
    }

    @Test
    public void RegisterAccount() {
        CreateTestAccount();

        String expectedTitle = "User " + username + "1 was successfully created.";
        String actualTitle = driver.findElement(By.id("notice")).getText();
        assertEquals(expectedTitle, actualTitle);

        DeleteTestAccount();
    }

    @Test
    public void LoginToAdmin() {
        login(username, password);
        goToPage("Admin");

        String expectedTitle = "Logged in user: " + username + " Edit Delete"; // check if logged in user is correct
        String actualTitle = driver.findElement(By.id("admin")).getText();
        assertEquals(expectedTitle, actualTitle);

        logout();
    }

    @Test(expected = NoSuchElementException.class) // passes when element is not found that indicates you are logged in
    public void LogoutFromAdmin() {
        login(username, password); // by previous test, login works
        logout();

        goToPage("Admin");
        driver.findElement(By.id("admin")).getText();
    }

    @Test
    public void DeleteAccount() {
        CreateTestAccount(); // by previous test, we know creating account works
        DeleteTestAccount();
        login(username + "1", password); // by previous test, login works

        String expectedTitle = "Invalid user/password combination";
        String actualTitle = driver.findElement(By.id("notice")).getText();

        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    public void AddProducts() {
        login(username, password);
        CreateTestProduct();

        By productButtonXpath = By.cssSelector("#" + productTitle + " a[action='show']");
        driver.findElement(productButtonXpath).click();

        Assert.assertTrue(driver.getPageSource().contains(productTitle));
        Assert.assertTrue(driver.getPageSource().contains(productType));
        Assert.assertTrue(driver.getPageSource().contains(productPrice));

        DeleteTestProduct();
        logout();
    }

    @Test
    public void EditProducts() {
        login(username, password);
        CreateTestProduct();

        By productButtonXpath = By.xpath("/html/body/div[4]/div[2]/div/table/tbody/tr[5]/td[3]/a");
        driver.findElement(productButtonXpath).click();
        driver.findElement(By.id("product_title")).clear();
        driver.findElement(By.id("product_title")).sendKeys("NewTitle");

        By createProductButtonXpath = By.cssSelector("input[type=submit]");
        driver.findElement(createProductButtonXpath).click();
        String expectedTitle = "Product was successfully updated.";
        String actualTitle = driver.findElement(By.id("notice")).getText();

        assertEquals(expectedTitle, actualTitle);

        goToPage("Products");
        By deleteProductButtonXpath = By.cssSelector("#NewTitle a[data-method='delete']");
        driver.findElement(deleteProductButtonXpath).click();

        logout();
    }

    @Test(expected = NoSuchElementException.class)
    public void DeleteProducts() {
        login(username, password);
        CreateTestProduct(); // Know previously that this part works
        DeleteTestProduct();

        driver.findElement(By.id("Meditations"));
        logout();
    }

    @Test
    public void DuplicateAccountCreate() {
        CreateTestAccount();
        logout();
        CreateTestAccount();
        String expectedTitle = "1 error prohibited this user from being saved:\n" +
                "Name has already been taken";
        String actualTitle = driver.findElement(By.id("error_explanation")).getText();
        assertEquals(expectedTitle, actualTitle);
        login(username + "1",password);
        DeleteTestAccount();
    }

    @Test
    public void PriceShouldBeNumber() {
        login(username,password);
        productPrice = "test";
        CreateTestProduct();

        String expectedTitle = "1 error prohibited this product from being saved:\n" +
                "Price is not a number";
        String actualTitle = driver.findElement(By.id("error_explanation")).getText();
        assertEquals(expectedTitle, actualTitle);

        logout();
        productPrice = "15";
    }

    @Test
    public void DuplicateProductCreate() {
        login(username,password);
        CreateTestProduct();
        CreateTestProduct();
        String expectedTitle = "1 error prohibited this product from being saved:\n" +
                "Title has already been taken";
        String actualTitle = driver.findElement(By.id("error_explanation")).getText();
        assertEquals(expectedTitle, actualTitle);
        DeleteTestProduct();
        logout();
    }

    @Test
    public void checkCategoryAfterUpdate() throws InterruptedException {
        login(username,password);
        goToPage("Products");
        String before = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/table/tbody/tr[2]/td[2]/div/span")).getText();
        TimeUnit.SECONDS.sleep(1);
        driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/table/tbody/tr[2]/td[3]/a")).click();
        TimeUnit.SECONDS.sleep(1);
        driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[5]/input")).click();
        goToPage("Products");
        String after = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/table/tbody/tr[3]/td[2]/div/span")).getText();

        TimeUnit.SECONDS.sleep(5);
        assertEquals(before,after);
    }



    public void CreateTestAccount() {
        goToPage("Register");
        driver.findElement(By.id("user_name")).sendKeys(username + "1");
        driver.findElement(By.id("user_password")).sendKeys(password);
        driver.findElement(By.id("user_password_confirmation")).sendKeys(password);
        By registerButtonXpath = By.xpath("//input[@value='Create User']");
        driver.findElement(registerButtonXpath).click();
    }

    public void DeleteTestAccount() {
        goToPage("Admin");
        By deletePath = By.linkText("Delete");
        driver.findElement(deletePath).click();
    }

    public void CreateTestProduct() {
        driver.findElement(By.id("new_product_div")).click();
        driver.findElement(By.id("product_title")).sendKeys(productTitle);
        driver.findElement(By.id("product_description")).sendKeys(productDescription);
        driver.findElement(By.id("product_price")).sendKeys(productPrice);
        Select dropdown = new Select(driver.findElement(By.id("product_prod_type")));
        dropdown.selectByValue(productType);

        By createProductButtonXpath = By.cssSelector("input[type=submit]");
        driver.findElement(createProductButtonXpath).click();
    }

    public void DeleteTestProduct() {
        goToPage("Products");
        By deleteProductButtonXpath = By.cssSelector("#" + productTitle + " a[data-method='delete']");
        driver.findElement(deleteProductButtonXpath).click();
    }
}
