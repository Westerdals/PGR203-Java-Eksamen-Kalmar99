package no.kristiania.taskManager;

import no.kristiania.db.Member;
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

public class ProjectMemberHttpController implements HttpController {

    private ProjectMemberDao pmemberDao;

    public ProjectMemberHttpController(ProjectMemberDao pmemberDao)
    {
        this.pmemberDao = pmemberDao;
    }

    @Override
    public void handle(String requestAction, String requestPath, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException {
        if(requestAction.equals("POST"))
        {

            requestParameters = HttpServer.parseQueryString(body);

            String pname = URLDecoder.decode(requestParameters.get("TaskSelect"), StandardCharsets.UTF_8.toString());
            String mname = URLDecoder.decode(requestParameters.get("memberSelect"), StandardCharsets.UTF_8.toString());



            ProjectMember pmember = new ProjectMember(pname,mname);

            pmemberDao.insert(pmember,"insert into project_member (projectName,memberName) values (?,?)");
            outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                    "Location: http://localhost:8080/\r\n"+
                    "Connection: close\r\n"+
                    "\r\n").getBytes());
            return;
        }


    }
}
