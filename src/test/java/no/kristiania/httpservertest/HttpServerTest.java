package no.kristiania.httpservertest;


import no.kristiania.httpserver.HttpClient;
import no.kristiania.httpserver.HttpClientResponse;
import no.kristiania.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpServerTest {


    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpServer(0);
        server.start();
    }
    @Test
    void shouldReturnStatusCode200() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(),"/echo");
        assertEquals(200,client.execute().getStatusCode());

    }
    @Test
    void shouldReturnStatusCode401() throws IOException {
        HttpClient client = new HttpClient("localhost",server.getPort(),"/echo?status=401");
        assertEquals(401,client.execute().getStatusCode());

    }

    @Test
    void shouldReturnHeaders() throws IOException
    {
        HttpClient client = new HttpClient("localhost",server.getPort(),"/echo?status=302&location=http://www.example.com");
        HttpClientResponse response = client.execute();
        assertEquals(302,response.getStatusCode());
        assertEquals("http://www.example.com",response.getHeader("location"));
    }
    @Test
    void shouldReturnBody() throws IOException
    {
        HttpClient client = new HttpClient("localhost",server.getPort(),"/echo?body=HelloWorld");
        HttpClientResponse response = client.execute();
        assertEquals("HelloWorld",response.getBody());

    }
    @Test
    void shouldParsePostParameters() throws IOException {

        String formBody = "content-type=text/html&body=foobar";
        HttpClient client = new HttpClient("localhost",server.getPort(),"/echo?" + formBody);
        client.setRequestHeader("content-type","application/x-www-form-urlencoded");
        client.setBody(formBody);
        HttpClientResponse response = client.execute("POST");
        assertThat(response.getHeader("content-type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("foobar");
    }
}
