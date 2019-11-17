package no.kristiania.taskManager;

import no.kristiania.db.Member;
import no.kristiania.db.ProjectDao;
import no.kristiania.db.ProjectMember;
import no.kristiania.db.ProjectMemberDao;
import no.kristiania.httpserver.HttpController;
import no.kristiania.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
            String[] target = requestPath.split("/");
            if (requestAction.equals("POST")) {
                requestParameters = HttpServer.parseQueryString(body);

                if(target[3].equals("remove"))
                {
                    removeMember(outputStream,requestParameters);
                } else {


                    String pname = URLDecoder.decode(requestParameters.get("TaskSelect"), StandardCharsets.UTF_8.toString());
                    String mname = URLDecoder.decode(requestParameters.get("memberSelect"), StandardCharsets.UTF_8.toString());


                    ProjectMember pmember = new ProjectMember(mname, pname);

                    pmemberDao.insert(pmember, "insert into project_member (projectName,memberName) values (?,?)");
                    redirect(outputStream);
                    return;
                }
            }

            //create response
            String responseBody = getBody();
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-length: " + responseBody.length() + "\r\n" +
                    "Content-type: text/html\r\n" +
                    "Connection: close\r\n" +
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

    private void removeMember(OutputStream outputStream, Map<String, String> requestParameters) throws IOException {
        String name = URLDecoder.decode(requestParameters.get("name"), StandardCharsets.UTF_8.toString());
        String projectName = URLDecoder.decode(requestParameters.get("task"), StandardCharsets.UTF_8.toString());
        pmemberDao.removeMember(name,projectName);
        redirect(outputStream);
    }

    private void redirect(OutputStream outputStream) throws IOException {
        outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                "Location: http://localhost:8080/\r\n" +
                "Connection: close\r\n" +
                "\r\n").getBytes());
        return;
    }


    public String getBody() throws SQLException {
        String body = pmemberDao.listAllWithMembers("SELECT projects.name,project_status.name as status , project_member.membername\n" +
                "    FROM projects\n" +
                "    JOIN project_member ON projects.name = projectname\n" +
                "    JOIN project_status ON projects.status = project_status.id;").stream()
                .map(p -> String.format("<tr> <td>%s</td> <td>%s</td> <td>%s</td> </tr>",p.getName(),p.getStatusname(),p.getMembers()))
                .collect(Collectors.joining(""));
        return body;
    }

}



