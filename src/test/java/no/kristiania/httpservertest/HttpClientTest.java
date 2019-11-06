package no.kristiania.httpservertest;


import no.kristiania.httpserver.HttpClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {


    @Test
    void shouldExecuteHttpRequest() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo");
        assertEquals(200,client.execute().getStatusCode());
    }
    @Test
    void shouldReadStatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?status=401");
        assertEquals(401,client.execute().getStatusCode());
    }
    @Test
    void shouldReadHeaders() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?content-type=text/plain");
        assertEquals("text/plain; charset=utf-8",client.execute().getHeader("Content-type"));
    }
    @Test
    void shouldReadContentLenght() throws IOException
    {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=hello+world!");
        assertEquals(12,client.execute().getContentLength());
    }
    @Test
    void shouldReadBody() throws IOException
    {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=hello+world!");
        assertEquals("hello world!",client.execute().getBody());
    }



}
