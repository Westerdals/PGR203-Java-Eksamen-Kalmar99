package no.kristiania.taskManager;

import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.httpserver.HttpController;
import no.kristiania.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class MembersHttpController implements HttpController {

    public static final Logger logger = LoggerFactory.getLogger(MembersHttpController.class);
    MemberDao memberDao;

    public MembersHttpController(MemberDao memberDao) {

        this.memberDao = memberDao;
    }

    @Override
    public void handle(String requestAction, String requestPath, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException {

        try {
            String[] target = requestPath.split("/");
            if(requestAction.equals("POST")) {

                requestParameters = HttpServer.parseQueryString(body);
                if(target[3].equals("add")) {
                    addMember(outputStream, requestParameters);
                    return;
                } else if (target[3].equals("remove")) {
                    //remove
                    removeMember(outputStream,requestParameters);
                } else if (target[3].equals("edit")) {
                    //Edit
                    editMember(outputStream,requestParameters);
                }

            }
            String responseBody = "";

            if(target[3].equals("select")) {
                responseBody = getSelectBody();
            } else { responseBody = getBody(); }
            sendResponse(outputStream, responseBody, "200 OK", "text/html");

        } catch (SQLException e) {
            String message = e.toString();
            logger.error("Critical ERROR:{}",message);
            sendResponse(outputStream,message,"501 Internal server error","text/html");
        }

    }

    private void editMember(OutputStream outputStream, Map<String, String> requestParameters) throws IOException {
        int targetID = Integer.valueOf(URLDecoder.decode(requestParameters.get("Id"), StandardCharsets.UTF_8.toString()));
        String newName = URLDecoder.decode(requestParameters.get("newName"), StandardCharsets.UTF_8.toString());
        String newMail = URLDecoder.decode(requestParameters.get("newMail"), StandardCharsets.UTF_8.toString());
        String oldName = URLDecoder.decode(requestParameters.get("oldName"), StandardCharsets.UTF_8.toString());
        memberDao.editMember(targetID,newName,newMail,oldName);
        redirect(outputStream);
    }

    private void removeMember(OutputStream outputStream, Map<String, String> requestParameters) throws IOException {
        int id = Integer.valueOf(URLDecoder.decode(requestParameters.get("id"), StandardCharsets.UTF_8.toString()));
        String name = URLDecoder.decode(requestParameters.get("name"), StandardCharsets.UTF_8.toString());
        memberDao.removeObject(id,name);
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
        String body = memberDao.listAll("SELECT * FROM Members").stream()
                .map(p -> String.format("<option id='%s'> %s </option>",p.getId(),p.getName()))
                .collect(Collectors.joining(""));
        return body;
    }

    private void addMember(OutputStream outputStream, Map<String, String> requestParameters) throws IOException {
        String name = URLDecoder.decode(requestParameters.get("memberName"), StandardCharsets.UTF_8.toString());
        String email = URLDecoder.decode(requestParameters.get("email"), StandardCharsets.UTF_8.toString());


        Member member = new Member(name, email);


        logger.info("Inserted Member with name: {} and email: {} to members database", member.getName(), member.getEmail());
        memberDao.insert(member, "insert into members (name,email) values (?,?)");

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
        String body = memberDao.listAll("SELECT * FROM Members").stream()
                .map(p -> String.format("<tr> <td>%s</td> <td>%s</td> <td>%s</td> </tr>",p.getId(),p.getName(),p.getEmail()))
                .collect(Collectors.joining(""));
        return body;
    }



}
