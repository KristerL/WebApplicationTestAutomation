import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

public class UserTest extends TestHelper{
    @Before
    public void userSetup(){driver.get(baseUrl);}

    @Test
    public void AddToCart(){
        By productButtonXpath = By.xpath("/html/body/div[4]/div[2]/div[3]/div[2]/form/input[1]");
        driver.findElement(productButtonXpath).click();
        //Getting information in store needed later
        By InStoreXpath1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[2]");
        String InStoreName = driver.findElement(InStoreXpath1).getText();
        By InStoreXpath2 = By.xpath("/html/body/div[4]/div[2]/div[3]/div[2]/span");
        String InStorePrice = driver.findElement(InStoreXpath2).getText();

        //Getting information in cart
        By CartButtonXpath1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[2]");
        String InCartName = driver.findElement(CartButtonXpath1).getText();
        By CartButtonXpath2 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[3]");
        String InCartPrice = driver.findElement(CartButtonXpath2).getText();
        System.out.println(InCartPrice);


        assertEquals(InStoreName, InCartName);
        assertEquals(InStorePrice, InCartPrice);
    }

    @Test
    public void IncreaseItemQuantity() throws InterruptedException {
        addItemToCart(4);

        By quantity1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[1]");//quantity
        String quantityBefore = driver.findElement(quantity1).getText();
        String quantityBeforeParse = quantityBefore.replace("×","");
        By price1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[3]");//price
        By total1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[2]/td[2]");//total
        String priceBefore = driver.findElement(price1).getText();
        String totalBefore = driver.findElement(total1).getText();
        String priceBefore1 = priceBefore.replace("€","");
        String totalBefore1 = totalBefore.replace("€","");
        //parseing
        int quantityBeforeP = Integer.parseInt(quantityBeforeParse);
        float priceBeforeP = Float.parseFloat(priceBefore1);
        float totalBeforeP = Float.parseFloat(totalBefore1);

        //increasing quantity by one
        By increaseButtonXpath = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[5]/a");
        driver.findElement(increaseButtonXpath).click();
        TimeUnit.SECONDS.sleep(3);//waiting to website to recognize the increment, otherwise takes elements instantly
        //elements
        By quantity2 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[1]");//quantity
        String quantityAfter = driver.findElement(quantity2).getText();
        String quantityAfterParse = quantityAfter.replace("×","");
        By price2 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[3]");//price
        By total2 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[2]/td[2]");//total
        String priceAfter = driver.findElement(price2).getText();
        String totalAfter = driver.findElement(total2).getText();
        String priceAfter1 = priceAfter.replace("€","");
        String totalAfter1 = totalAfter.replace("€","");
        //parseing
        int quantityAfterP = Integer.parseInt(quantityAfterParse);
        float priceAfterP = Float.parseFloat(priceAfter1);
        float totalAfterP = Float.parseFloat(totalAfter1);

        assertEquals(quantityBeforeP+quantityBeforeP,quantityAfterP);
        assertEquals(priceBeforeP+priceBeforeP, priceAfterP);
        assertEquals(totalBeforeP+totalBeforeP,totalAfterP);

    }

    @Test
    public void DecreaseItemQuantity() throws InterruptedException {
        //adding two items, so quantity is 2
        addItemToCart(4);
        addItemToCart(4);
        TimeUnit.SECONDS.sleep(1);

        By quantity1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[1]");//quantity
        String quantityBefore = driver.findElement(quantity1).getText();
        String quantityBeforeParse = quantityBefore.replace("×","");


        //parseing
        int quantityBeforeP = Integer.parseInt(quantityBeforeParse);


        //increasing quantity by one
        By decreaseButtonXpath = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[4]/a");
        driver.findElement(decreaseButtonXpath).click();
        TimeUnit.SECONDS.sleep(3);//waiting to website to recognize the increment, otherwise takes elements instantly
        //elements
        By quantity2 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[1]");//quantity
        String quantityAfter = driver.findElement(quantity2).getText();
        String quantityAfterParse = quantityAfter.replace("×","");

        //parseing
        int quantityAfterP = Integer.parseInt(quantityAfterParse);

        assertEquals(quantityBeforeP-1,quantityAfterP);
    }

    @Test
    public void deleteItem() throws InterruptedException {
        //one of negative test
        addItemToCart(1);
        addItemToCart(2);
        addItemToCart(3);
        addItemToCart(4);
        int rand = random(1,4);//deciding to delete random item in cart

        By randItemDeleteXpath = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr["+Integer.toString(rand)+"]/td[4]/a");
        By randItemNameXpath = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr["+Integer.toString(rand)+"]/td[2]");
        String randItemName = driver.findElement(randItemNameXpath).getText();
        driver.findElement(randItemDeleteXpath).click();
        TimeUnit.SECONDS.sleep(2);

        By randDeletedItemeXpath = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr["+Integer.toString(rand)+"]/td[2]");
        String randDeletedItem = driver.findElement(randDeletedItemeXpath).getText();
        assertEquals(randItemName,randDeletedItem);//comparing names at the same position, so if it fails, it works as needed
    }


    @Test
    public void DeleteCart() throws InterruptedException {
        addItemToCart(1);
        addItemToCart(2);
        addItemToCart(1);
        addItemToCart(2);
        addItemToCart(3);
        addItemToCart(4);
        TimeUnit.SECONDS.sleep(1);
        By EmptyCartXpath = By.xpath("/html/body/div[4]/div[1]/div/form[1]/input[2]");
        driver.findElement(EmptyCartXpath).click();

        String actualTitle = driver.findElement(By.id("notice")).getText();
        String expectedTitle = "Cart successfully deleted.";
        assertEquals(expectedTitle,actualTitle);
    }


    @Test
    public void searchItem() {
        driver.findElement(By.id("search_input")).sendKeys("Web Application Testing Book");
        String expectedName = "Web Application Testing Book";

        String actualName = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div[6]/h3/a")).getText();
        assertEquals(expectedName,actualName);
    }
    @Test
    public void sortBySunglasses(){
        driver.findElement(By.xpath("/html/body/div[2]/div/ul/li[2]/a")).click();
        String currentURL = driver.getCurrentUrl();
        String wantedURL = "https://guarded-ridge-71080.herokuapp.com/store/filter?sort=Sunglasses";
        assertEquals(wantedURL,currentURL);
    }

    @Test
    public void sortByBooks(){
        driver.findElement(By.xpath("/html/body/div[2]/div/ul/li[3]/a")).click();
        String currentURL = driver.getCurrentUrl();
        String wantedURL = "https://guarded-ridge-71080.herokuapp.com/store/filter?sort=Books";
        assertEquals(wantedURL,currentURL);
    }

    @Test
    public void sortByOthers(){
        driver.findElement(By.xpath("/html/body/div[2]/div/ul/li[4]/a")).click();
        String currentURL = driver.getCurrentUrl();
        String wantedURL = "https://guarded-ridge-71080.herokuapp.com/store/filter?sort=Other";
        assertEquals(wantedURL,currentURL);
    }



    @Test
    public void purchase(){//peale place orderit on kogu summa valesti, 1 bug
        addItemToCart(1);
        addItemToCart(2);
        addItemToCart(3);
        addItemToCart(4);
        driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/form[2]/input")).click();
        driver.findElement(By.id("order_name")).sendKeys("Test");
        driver.findElement(By.id("order_address")).sendKeys("Address");
        driver.findElement(By.id("order_email")).sendKeys("Test@test.ut.ee");
        Select payment = new Select(driver.findElement(By.id("order_pay_type")));
        payment.selectByVisibleText("Check");

        driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[5]/input")).click();
        String actualResult = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/h3")).getText();
        String expectedResult = "Thank you for your order";
        assertEquals(expectedResult,actualResult);

    }

    @Test
    public void checkCart() throws InterruptedException {
        //checking that the price of item in cart stays the same, since quantity changes and cheking that total changes correctly
        addItemToCart(4);
        By price1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[3]");//price
        By total1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[2]/td[2]");//total
        String priceBefore = driver.findElement(price1).getText();
        String totalBefore = driver.findElement(total1).getText();
        String priceBefore1 = priceBefore.replace("€","");
        String totalBefore1 = totalBefore.replace("€","");
        //parseing
        float priceBeforeP = Float.parseFloat(priceBefore1);
        float totalBeforeP = Float.parseFloat(totalBefore1);

        addItemToCart(4);
        TimeUnit.SECONDS.sleep(1);
        //After adding the same item
        By price2 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[1]/td[3]");//price
        By total2 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[2]/td[2]");//total
        String priceAfter = driver.findElement(price2).getText();
        String totalAfter = driver.findElement(total2).getText();
        String priceAfter1 = priceAfter.replace("€","");
        String totalAfter1 = totalAfter.replace("€","");
        //parseing
        float priceAfterP = Float.parseFloat(priceAfter1);
        float totalAfterP = Float.parseFloat(totalAfter1);

        assertEquals(priceBeforeP,priceAfterP);//differes
        assertEquals(totalBeforeP*2,totalAfterP);
    }

    @Test
    public void checkCheckout() throws InterruptedException {
        addItemToCart(1);
        addItemToCart(1);
        TimeUnit.SECONDS.sleep(1);
        By total1 = By.xpath("/html/body/div[4]/div[1]/div/table/tbody/tr[2]/td[2]");//total from cart
        String totalBefore = driver.findElement(total1).getText();
        String totalBefore1 = totalBefore.replace("€","");
        float totalBeforeP = Float.parseFloat(totalBefore1);

        driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/form[2]/input")).click();
        driver.findElement(By.id("order_name")).sendKeys("Test");
        driver.findElement(By.id("order_address")).sendKeys("Address");
        driver.findElement(By.id("order_email")).sendKeys("Test@test.ut.ee");
        Select payment = new Select(driver.findElement(By.id("order_pay_type")));
        payment.selectByVisibleText("Check");
        TimeUnit.SECONDS.sleep(1);
        driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[5]/input")).click();
        TimeUnit.SECONDS.sleep(1);
        String finalSum = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/table/tbody/tr[2]/td[2]/strong")).getText();
        String BPsum = finalSum.replace("€","");
        float parseSum = Float.parseFloat(BPsum);

        assertEquals(totalBeforeP,parseSum);//differs from actual result

    }

    @Test
    public void neg_testCheckout(){
        addItemToCart(1);
        driver.findElement(By.xpath("/html/body/div[4]/div[1]/div/form[2]/input")).click();
        driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[5]/input")).click();
        String nameError = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[1]/ul/li[1]")).getText();
        String addressError = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[1]/ul/li[2]")).getText();
        String meilError = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[1]/ul/li[3]")).getText();
        String paymentError = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/form/div[1]/ul/li[4]")).getText();

        assertEquals("Name can't be blank",nameError);
        assertEquals("Address can't be blank", addressError);
        assertEquals("Email can't be blank",meilError);
        assertEquals("Pay type is not included in the list",paymentError);


    }

    public void addItemToCart(int n){//checked with AddToCart() test
        switch (n){
            case 1:
                By productButtonXpath1 = By.xpath("/html/body/div[4]/div[2]/div[3]/div[2]/form/input[1]");//B45593 Sunglasses
                driver.findElement(productButtonXpath1).click();
                break;
            case 2:
                By productButtonXpath2 = By.xpath("/html/body/div[4]/div[2]/div[4]/div[2]/form/input[1]");//Sunglasses 2AR
                driver.findElement(productButtonXpath2).click();
                break;
            case 3:
                By productButtonXpath3 = By.xpath("/html/body/div[4]/div[2]/div[5]/div[2]/form/input[1]");//Sunglasses B45593
                driver.findElement(productButtonXpath3).click();
                break;
            case 4:
                By productButtonXpath4 = By.xpath("/html/body/div[4]/div[2]/div[6]/div[2]/form/input[1]");//Web Application Testing Book
                driver.findElement(productButtonXpath4).click();
                break;
        }

    }
    public static int random(int a, int b){
        //generates random int from a to b
        return (int)(Math.round(Math.random()*(b-a)+a));
    }


}
