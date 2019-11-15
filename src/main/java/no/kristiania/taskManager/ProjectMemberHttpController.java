package no.kristiania.taskManager;

import no.kristiania.db.Member;
import no.kristiania.db.ProjectDao;
import no.kristiania.db.ProjectMember;
import no.kristiania.db.ProjectMemberDao;
import no.kristiania.httpserver.HttpController;
import no.kristiania.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectMemberHttpController implements HttpController {

    private ProjectMemberDao pmemberDao;

    public ProjectMemberHttpController(ProjectMemberDao pmemberDao)
    {
        this.pmemberDao = pmemberDao;
    }

    @Override
    public void handle(String requestAction, String requestPath, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException {

        try {
            if (requestAction.equals("POST")) {

                requestParameters = HttpServer.parseQueryString(body);

                String pname = URLDecoder.decode(requestParameters.get("TaskSelect"), StandardCharsets.UTF_8.toString());
                String mname = URLDecoder.decode(requestParameters.get("memberSelect"), StandardCharsets.UTF_8.toString());


                ProjectMember pmember = new ProjectMember(mname,pname);

                pmemberDao.insert(pmember, "insert into project_member (projectName,memberName) values (?,?)");
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
                return;
            }

            //Handle Member Request!
            //create response
            String responseBody;
            String statusCode = "200";
            String contentType = "text/html";

            responseBody = getBody();

            outputStream.write(("HTTP/1.1 " + statusCode + " OK\r\n" +
                    "Content-length: " + responseBody.length() + "\r\n" +
                    "\r\n" +
                    responseBody).getBytes());

        } catch (SQLException e) {
            String message = e.toString();

            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes());
        }

    }

    public String getBody() throws SQLException {
        String body = pmemberDao.listAllWithMembers("SELECT name,status,project_member.membername FROM projects JOIN project_member ON name = projectname;").stream()
                .map(p -> String.format("<tr> <td>%s</td> <td>%s</td> <td>%s</td> </tr>",p.getName(),p.getStatus(),p.getMembers()))
                .collect(Collectors.joining(""));
        return body;
    }

}



