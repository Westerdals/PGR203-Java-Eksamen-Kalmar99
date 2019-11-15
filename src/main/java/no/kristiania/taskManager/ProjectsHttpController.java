package no.kristiania.taskManager;


import no.kristiania.db.Project;
import no.kristiania.db.ProjectDao;
import no.kristiania.httpserver.HttpController;
import no.kristiania.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectsHttpController implements HttpController {


    ProjectDao projectDao;

    public ProjectsHttpController(ProjectDao projectDao) {

        this.projectDao = projectDao;
    }

    @Override
    public void handle(String requestAction, String requestPath, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException {


        try {
            String[] target = requestPath.split("/");
            if(requestAction.equals("POST"))
            {
                if(target[3].equals("add"))
                {
                    addProject(outputStream, body);
                    return;
                } else if (target[3].equals("remove"))
                {
                    //Remove
                }

            }

            String responseBody = "";
            if(target[3].equals("select"))
            {
                responseBody = getSelectBody();
            } else { responseBody = getBody(); }
            //create response
            sendResponse(outputStream, responseBody, "text/html", "200 OK");

        } catch (SQLException e) {
            String message = e.toString();
            sendResponse(outputStream, message, "text/html", "501 Internal server error");
        }

    }

    private void sendResponse(OutputStream outputStream, String responseBody, String contentType, String statusCode) throws IOException {
        outputStream.write(("HTTP/1.1 " + statusCode + "\r\n" +
                "Content-type:" + contentType + "\r\n" +
                "Content-length: " + responseBody.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseBody).getBytes());
    }

    private void addProject(OutputStream outputStream, String body) throws IOException {
        Map<String, String> requestParameters;
        requestParameters = HttpServer.parseQueryString(body);


        String name = URLDecoder.decode(requestParameters.get("projectName"), StandardCharsets.UTF_8.toString());
        String status = URLDecoder.decode(requestParameters.get("projectStatus"),StandardCharsets.UTF_8.toString());

        Project project = new Project(name,status);
        System.out.println("Created new Project with name: " + project.getName() + "And Status: " + project.getStatus());
        projectDao.insert(project,"insert into projects (name,status) values (?,?)");
        outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                "Location: http://localhost:8080/\r\n"+
                "Connection: close\r\n"+
                "\r\n").getBytes());
        return;
    }

    public String getBody() throws SQLException {
        String body = projectDao.listAll("SELECT * FROM projects").stream()
                .map(p -> String.format("<tr> <td>%s</td> <td>%s</td> <td>%s</td> </tr>",p.getId(),p.getName(),p.getStatus()))
                .collect(Collectors.joining(""));
        return body;
    }

    public String getSelectBody() throws SQLException {
        String body = projectDao.listAll("SELECT * FROM projects").stream()
                .map(p -> String.format("<option id='%s'>%s</option>",p.getId(),p.getName()))
                .collect(Collectors.joining(""));
        return body;
    }


    }

