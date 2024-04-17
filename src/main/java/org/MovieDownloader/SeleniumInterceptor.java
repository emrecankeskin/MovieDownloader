package org.MovieDownloader;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class SeleniumInterceptor {

    WebDriver driver;
    BrowserMobProxyServer proxy;
    Har har;
    Proxy seleniumProxy;
    String hostIp;
    DesiredCapabilities seleniumCapabilities;
    ChromeOptions options;

    ArrayList<String> m3u8FilesList = new ArrayList<>();
    List<String> filesList = new ArrayList<>();
    List<HarEntry> entries;

    public void openWebSite(String websiteUrl){

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        proxy = new BrowserMobProxyServer();

        // Does your website request require custom headers?
        // (Cookies, User-agent, jwt, etc) Add them here
        proxy.addRequestFilter((request, contents, messageInfo) -> {
            request.headers().add("Connection", "keep-alive");
            request.headers().add("Upgrade-Insecure-Requests", "1");
            request.headers().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");
            request.headers().add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            request.headers().add("Accept-Language", "en-US,en;q=0.9,es;q=0.8");
            return null;
        });

        proxy.setTrustAllServers(true);
        proxy.start();

        seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        try {
            hostIp = Inet4Address.getLocalHost().getHostAddress();
            seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        seleniumCapabilities = new DesiredCapabilities();
        seleniumCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);


        /*
        * Arguments of Chrome
        * */
        options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-debugging-port=9222");
        options.merge(seleniumCapabilities);

        driver = new ChromeDriver(options);

        // Capture all event types
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.REQUEST_COOKIES, CaptureType.REQUEST_CONTENT,
                CaptureType.REQUEST_BINARY_CONTENT, CaptureType.RESPONSE_HEADERS, CaptureType.RESPONSE_COOKIES, CaptureType.RESPONSE_CONTENT,
                CaptureType.RESPONSE_BINARY_CONTENT);

        // Create HTTP Archive (HAR) file for http tracing. (Script will attempt to capture all m3u8 requests produced from website loading)
        proxy.newHar("harCapture");
        har = proxy.getHar();

        // Start capture
        driver.get(websiteUrl);

    }

    public void closeBrowser(){
        proxy.stop();
        driver.quit();
    }

    public ArrayList<String> getLinks(){

        //proxy.stop();

        // Collect all m3u8 http requests in this list
        try {
            entries = proxy.getHar().getLog().getEntries();
            for (HarEntry entry : entries) {

                if (entry.getRequest().getUrl().contains("m3u8") || entry.getRequest().getUrl().contains("ts") || entry.getRequest().getUrl().contains("seg")) {
                    m3u8FilesList.add(entry.getRequest().getUrl());
                }
            }
        }catch (Exception e){
            System.out.println("[LOG] ERROR IN getLinks()");
        }
        return m3u8FilesList;
    }

    public List<String> getAllLinks(){

        //proxy.stop();

        entries = proxy.getHar().getLog().getEntries();
        for (HarEntry entry : entries) {
            //if(entry.getRequest().getUrl().endsWith(".m3u8") || entry.getRequest().getUrl().endsWith(".vtt"))
                filesList.add(entry.getRequest().getUrl());
        }
        return filesList;
    }

}
