import com.machinepublishers.jbrowserdriver.JBrowserDriver
import com.machinepublishers.jbrowserdriver.RequestHeaders
import com.machinepublishers.jbrowserdriver.Settings
import com.machinepublishers.jbrowserdriver.Timezone
import com.machinepublishers.jbrowserdriver.UserAgent

class JBrowserDriverTest {

    /**
     * Let this program running at least for 5 minutes to see the ever growing heap.
     * Visualisation of heap space is possible with visualVM.
     * The cache(Boolean) setting has no impact on this behaviour.
     * @param args
     */
    public static void main(String[] args) {
        JBrowserDriver webdriver = new JBrowserDriver(Settings.builder().connectTimeout(10000).socketTimeout(30000).connectionReqTimeout(30000)
                .timezone(Timezone.EUROPE_BERLIN).maxRouteConnections(100).cache(true)
                .userAgent(UserAgent.CHROME).headless(true).ssl("trustanything").hostnameVerification(false).quickRender(true)
                .javascript(false).javaOptions("-Xmx1g", "-XX:+UseG1GC", "-server", "-XX:+AggressiveOpts", "-XX:+UseStringDeduplication")
                .requestHeaders(getJBrowserDriverRequestHeaders())
                .build())

        // read asins from file
        Integer counter = 0
        String url
        new File(JBrowserDriverTest.getResource("asins.csv").toURI()).eachLine { asin ->
            url = "https://www.amazon.de/dp/" + asin + "?ie=UTF8&*Version*=1&*entries*=0"
            println "#" + counter + " requesting url " + url
            webdriver.get("https://www.amazon.de/dp/" + asin + "?ie=UTF8&*Version*=1&*entries*=0")
            counter++
        }
    }

    public static RequestHeaders getJBrowserDriverRequestHeaders() {
        LinkedHashMap<String, String> headersTmp = new LinkedHashMap<>();
        headersTmp.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headersTmp.put("Upgrade-Insecure-Requests", "1");
        headersTmp.put("User-Agent", RequestHeaders.DYNAMIC_HEADER);
        headersTmp.put("Referer", RequestHeaders.DYNAMIC_HEADER);
        headersTmp.put("Accept-Encoding", "gzip, deflate, sdch");
        headersTmp.put("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
        headersTmp.put("Cookie", RequestHeaders.DYNAMIC_HEADER);
        headersTmp.put("Host", RequestHeaders.DYNAMIC_HEADER);
        headersTmp.put("Proxy-Connection", RequestHeaders.DROP_HEADER);
        return new RequestHeaders(headersTmp);
    }
}
