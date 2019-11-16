package no.kristiania.taskManager;

import no.kristiania.db.Status;
import no.kristiania.db.StatusDao;
import no.kristiania.httpserver.HttpController;
import no.kristiania.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CDATASection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class StatusHttpController implements HttpController {

    private StatusDao statusDao;
    public static final Logger logger = LoggerFactory.getLogger(StatusHttpController.class);

    StatusHttpController(StatusDao statusDao)
    {
        this.statusDao = statusDao;
    }

    @Override
    public void handle(String requestAction, String requestPath, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException {

        try {
            String[] target = requestPath.split("/");
            if(requestAction.equals("POST"))
            {

                requestParameters = HttpServer.parseQueryString(body);
                if(target[3].equals("add"))
                {
                    addStatus(outputStream, requestParameters);
                    return;
                } else if (target[3].equals("remove"))
                {
                    //remove
                    removeStatus(outputStream,requestParameters);
                } else if (target[3].equals("edit"))
                {
                    editStatus(outputStream,requestParameters);
                }

            }
            String responseBody = "";

            if(target[3].equals("select"))
            {
                responseBody = getSelectBody();
            } else { responseBody = getBody(); }
            sendResponse(outputStream, responseBody, "200 OK", "text/html");

        } catch (SQLException e) {
            String message = e.toString();
            logger.error("Critical ERROR:{}",message);
            sendResponse(outputStream,message,"501 Internal server error","text/html");
        }

    }

    private void editStatus(OutputStream outputStream, Map<String, String> requestParameters) throws IOException {

        int projectID = Integer.valueOf(URLDecoder.decode(requestParameters.get("sid"), StandardCharsets.UTF_8.toString()));
        int newStatusID = Integer.valueOf(URLDecoder.decode(requestParameters.get("sid2"), StandardCharsets.UTF_8.toString()));

        statusDao.edit(projectID,newStatusID);

        redirect(outputStream);

    }

    private void removeStatus(OutputStream outputStream, Map<String, String> requestParameters) throws IOException {

        int id = Integer.valueOf(URLDecoder.decode(requestParameters.get("id"), StandardCharsets.UTF_8.toString()));
        String name = URLDecoder.decode(requestParameters.get("name"), StandardCharsets.UTF_8.toString());

        statusDao.removeObject(id,name);
        redirect(outputStream);
    }

    private void sendResponse(OutputStream outputStream, String responseBody, String statusCode, String contentType) throws IOException {
        outputStream.write((
                "HTTP/1.1 " + statusCode + "\r\n" +
                        "Content-type:" + contentType + "\r\n" +
                        "Content-length: " + responseBody.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        responseBody).getBytes());
    }

    private String getSelectBody() throws SQLException {
        String body = statusDao.listAll("SELECT * FROM project_status").stream()
                .map(p -> String.format("<option id='%s'> %s </option>",p.getId(),p.getName()))
                .collect(Collectors.joining(""));
        return body;
    }

    private void addStatus(OutputStream outputStream, Map<String, String> requestParameters) throws IOException {
        String name = URLDecoder.decode(requestParameters.get("name"), StandardCharsets.UTF_8.toString());


        Status status = new Status(name);

        statusDao.insert(status, "insert into project_status (name) values (?)");

        redirect(outputStream);
        return;
    }

    private void redirect(OutputStream outputStream) throws IOException {
        outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                "Location: http://localhost:8080/\r\n" +
                "Connection: close\r\n" +
                "\r\n").getBytes());
        return;
    }

    public String getBody() throws SQLException {
        String body = statusDao.listAll("SELECT * FROM Members").stream()
                .map(p -> String.format("<tr> <td>%s</td> <td>%s</td> </tr>",p.getId(),p.getName()))
                .collect(Collectors.joining(""));
        return body;
    }


}
