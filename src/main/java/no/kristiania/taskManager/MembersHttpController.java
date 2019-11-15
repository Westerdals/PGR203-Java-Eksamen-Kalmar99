package no.kristiania.taskManager;

import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.httpserver.HttpController;
import no.kristiania.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
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
            if(requestAction.equals("POST"))
            {

                requestParameters = HttpServer.parseQueryString(body);

                String name = URLDecoder.decode(requestParameters.get("memberName"), StandardCharsets.UTF_8.toString());
                String email = URLDecoder.decode(requestParameters.get("email"), StandardCharsets.UTF_8.toString());



                Member member = new Member(name,email);


                logger.info("Inserted Member with name: {} and email: {} to members database",member.getName(),member.getEmail());

                memberDao.insert(member,"insert into members (name,email) values (?,?)");
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/\r\n"+
                        "Connection: close\r\n"+
                        "\r\n").getBytes());
                return;
            }

            String responseBody = getBody();
            String statusCode = "200";
            String contentType = "text/html";

            outputStream.write(("HTTP/1.1 " + statusCode + " OK\r\n" +
                    "Content-length: " + responseBody.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    responseBody).getBytes());

        } catch (SQLException e) {
            String message = e.toString();
            logger.error("Critical ERROR:{}",message);


            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes());
        }

    }

    public String getBody() throws SQLException {
        String body = memberDao.listAll("SELECT * FROM Members").stream()
                .map(p -> String.format("<tr> <td>%s</td> <td>%s</td> <td>%s</td> </tr>",p.getId(),p.getName(),p.getEmail()))
                .collect(Collectors.joining(""));
        return body;
    }

}
