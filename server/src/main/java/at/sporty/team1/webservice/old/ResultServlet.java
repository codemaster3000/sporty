package at.sporty.team1.webservice.old;

import at.sporty.team1.webservice.old.ResultPersistentBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by f00 on 15.12.15.
 */
@WebServlet(urlPatterns = {"/resultService"})
public class ResultServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();

        ResultPersistentBean resultPersistentBean = new ResultPersistentBean();

        out.println(resultPersistentBean.getResult(req.getQueryString()));


//        out.println("<ns:sayHelloResponse xmlns:ns=\"http://hellosrv\">");
//        out.println("<ns:return>Hello " + req.getParameter("sender") +
//                " from HelloRsServlet</ns:return>");
//        out.println("</ns:sayHelloResponse>");
    }
}
