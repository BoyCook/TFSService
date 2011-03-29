package org.cccs.tfs.web;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Craig Cook
 * Date: May 14, 2010
 * Time: 9:58:56 AM
 */
@SuppressWarnings({"unchecked"})
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JettyIntegrationTestEnvironment {

    protected static final Logger log = LoggerFactory.getLogger(JettyIntegrationTestEnvironment.class);

    @Autowired
    protected XStreamMarshaller marshaller;

    @Autowired
    protected MarshallingHttpMessageConverter domainObjectConverter;

    protected static URL baseUrl;
    protected static String webXML = "src/test/resources/web.xml";
    protected static boolean overrideWebXML = true;
    protected static boolean printStream = false;
    protected String serviceBaseURL;
    protected RestTemplate client;
    protected RestTemplate wsClient;
    public static Server server;
    private static final HttpHeaders headers = new HttpHeaders();

    @BeforeClass
    public static void runOnce() {
        headers.add("Accept", "application/xml");
        headers.add("Content-Type", "application/xml");
        startWebService();
    }

    @Before
    public void beforeEach() {
        client = new RestTemplate();
        client.setMessageConverters(CollectionUtils.arrayToList(
                new HttpMessageConverter<?>[]{
                domainObjectConverter, new SourceHttpMessageConverter()
        }));
        wsClient = new RestTemplate();
        wsClient.setMessageConverters(CollectionUtils.arrayToList(
                new HttpMessageConverter<?>[]{
                domainObjectConverter, new SourceHttpMessageConverter()
        }));
        serviceBaseURL = baseUrl + "/service/";
    }

    @AfterClass
    public static void stopWebService() throws Exception {
        log.debug("Stopping jetty test instance");
        server.stop();
        log.debug("Jetty test instance stopped");
    }
    public static void startWebService() {
        try {
            server = new Server();
            SelectChannelConnector connector = new SelectChannelConnector();
            connector.setMaxIdleTime(30000);
            connector.setAcceptors(2);
            connector.setStatsOn(true);
            connector.setLowResourcesConnections(5000);
            server.addConnector(connector);
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setWar("src/main/webapp");

            if (overrideWebXML) {
                webapp.setDescriptor(webXML);
            }

            server.setHandler(webapp);
            log.debug("Starting jetty test instance...");
            server.start();
            log.debug("Jetty startup complete.");
            baseUrl = new URL("http://localhost:" + connector.getLocalPort());
            log.debug("Server started:  " + baseUrl);
        } catch (Exception e) {
            log.debug("Error starting web service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void login(final String user, final String password) {
        final String url = baseUrl + "/j_spring_security_check?j_username=" + user + "&j_password=" + password;
        System.out.println("Logging in as: " + url);

        if (headers.containsKey("Cookie")) {
            headers.remove("Cookie");
            httpSimple(baseUrl + "/j_spring_security_logout");
        }

        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                final HttpHeaders headers = request.getHeaders();
                headers.putAll(getHeaders());
            }
        };

        ResponseExtractor responseExtractor = new ResponseExtractor<Object>() {
            @Override
            public Object extractData(final ClientHttpResponse response) throws IOException {
                String location = response.getHeaders().get("Location").get(0);
                String jSessionId = location.substring(location.indexOf("jsessionid=") + 11);
                jSessionId = jSessionId.replaceAll("/", "");
                if (jSessionId.indexOf("?") > -1) {
                    jSessionId = jSessionId.substring(0, jSessionId.indexOf("?"));
                }
                System.out.println("Header location: " + location);
                System.out.println("Header jSessionId: " + jSessionId);
                headers.add("Cookie", "JSESSIONID=" + jSessionId);
                return null;
            }
        };

        client.execute(url, HttpMethod.POST, requestCallback, responseExtractor);
    }


    public Object http(String url, final Object data, final HttpMethod method) {
        int code = 200;

        switch (method) {
            case PUT: code = 201; break;
            case POST: code = 205; break;
            case DELETE: code = 205; break;
        }

        return http(url, data, method, code);
    }

    public Object http(String url, final Object data, final HttpMethod method, final int code) {
        return http(url, data, code, method, client);
    }

    /**
     * This takes the url to post to, and the object to post
     * It will automatically marshall the object, and add the XML to the request body
     * @param url
     * @param data
     * @param code
     * @param method
     * @param client
     * @return
     */
    public Object http(final String url, final Object data, final int code, final HttpMethod method, final RestTemplate client) {
        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                final HttpHeaders headers = request.getHeaders();
                headers.putAll(getHeaders());

                if (data != null) {
                    OutputStream outputStream = request.getBody();
                    marshal(data, outputStream);
                    System.out.println(outputStream.toString());
                }
            }
        };

        ResponseExtractor responseExtractor = new ResponseExtractor<Object>() {
            @Override
            public Object extractData(final ClientHttpResponse response) throws IOException {
                System.out.println("CSC Code: " + response.getStatusCode().value() + " - " + code + " - " + url);
                assertThat(response.getStatusCode().value(), is(equalTo(code)));

                final InputStream is = response.getBody();
                if (is.available() > 0) {
                    if (printStream) {
                        printStream(is);
                        printStream = false;
                    }
                    log.debug("### Trying to unmarshall stream: ");
                    return marshaller.unmarshal(new StreamSource(is));
                } else {
                    log.debug("### No stream to unmarshall");
                    return null;
                }
            }
        };

        return client.execute(url, method, requestCallback, responseExtractor);
    }

    private void printHeaders(HttpHeaders headers) {
        for (String key: headers.keySet()) {
            List<String> values = headers.get(key);

            for (String value: values) {
                System.out.println(format("Key: (%s) - Value: (%s)", key, value));
            }
        }
    }

    private void printStream(final InputStream is) throws IOException {
         int b;
         while ( ( b = is.read() ) != -1 ) {
             System.out.print((char)b);
         }
    }

    /**
     * Does a get on a url, and unmarshalls the object it gets back
     *
     * @param url
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object httpGet(String url) {
        return http(url, null, 200, HttpMethod.GET, client);
    }

    /**
     * This takes the url to DELETE to.
     * It will automaticall add an *ignored* XML payload to the request body and unmarshal the response.
     *
     * @param url
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object httpDelete(String url) {
        return http(url, null, HttpMethod.DELETE);
    }

    public void httpFail(final String url, final Object o, final HttpMethod method, final int code) {
        try {
            http(url, o, method);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode().value(), is(equalTo(code)));
        }
    }

    public void httpDeleteFail(final String url, final int code) {
        try {
            httpDelete(url);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode().value(), is(equalTo(code)));
        }
    }

    public void httpGetFail(final String url, final int code) {
        try {
            httpGet(url);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode().value(), is(equalTo(code)));
        }
    }

    public String httpRaw(final String url) {
        return httpRaw(url, null, HttpMethod.GET);
    }

    public void httpSimple(final String url) {
        wsClient.execute(serviceBaseURL + url, HttpMethod.GET,
            new RequestCallback() {
                @Override
                public void doWithRequest(final ClientHttpRequest request) throws IOException {
                    final HttpHeaders headers = request.getHeaders();
                    headers.putAll(getHeaders());
                }
            }, new ResponseExtractor<String>() {
                @Override
                public String extractData(final ClientHttpResponse response) throws IOException {
                    return null;
                }
            });
    }

    public String httpRaw(final String url, final Object data, final HttpMethod method) {
        final Object payload = (data == null) ? "<ignored />" : data;

        return (String)wsClient.execute(serviceBaseURL + url, method,
            new RequestCallback() {
                @Override
                public void doWithRequest(final ClientHttpRequest request) throws IOException {
                    final HttpHeaders headers = request.getHeaders();
                    headers.putAll(getHeaders());

                    if (method != HttpMethod.GET) {
                        marshaller.marshal(payload, new StreamResult(request.getBody()));
                    }
                }
            }, new ResponseExtractor<String>() {
                @Override
                public String extractData(final ClientHttpResponse response) throws IOException {
                    final InputStream is = response.getBody();
                    if (is.available() > 0) {
                        return IOUtils.toString(is);
                    } else {
                        return null;
                    }
                }
            });
    }

    protected static HttpHeaders getHeaders() {
        return headers;
    }

    private String marshal(final Object obj) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshal(obj, new BufferedOutputStream(bos));
        return bos.toString();
    }

    private void marshal(final Object obj, final OutputStream stream) throws IOException {
        final Result result = new StreamResult(stream);
        marshaller.marshal(obj, result);
    }
}
