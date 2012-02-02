package com.sun.faces.test.selenium.agnostic.shared;

import org.openqa.selenium.WebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.junit.Assert.*;

public class IndexPageIT {

    /**
     * Stores the webdriver instance.
     */
    private WebDriver driver;
    
    /**
     * Stores the base URL.
     */
    private String baseUrl;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver(true);
        baseUrl = System.getProperty("integration.url");
    }

    /**
     * Test going to index.jsp and simple.jsp and back.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testIndexPage() throws Exception {
        driver.get(baseUrl);
        assertEquals(baseUrl, driver.getCurrentUrl());
        driver.findElement(By.id("simple")).click();
        driver.findElement(By.linkText("Back")).click();;
    }

    /**
     * Cleanup after testing.
     * 
     * @throws Exception when a serious error occurs.
     */
    @After
    public void tearDown() throws Exception {
        driver.close();
    }
}
