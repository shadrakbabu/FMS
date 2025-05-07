package com.fms;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/staff", "root", "hope");

            String sql = """
            		SELECT a.HFM_STAFFNO, m.HFM_NAME, a.HFM_ACTION_TYPE, a.HFM_PERSON, a.HFM_ACTION_DATE, a.HFM_ACTION_TIME
            		FROM HR_FILE_ACTIONS a
            		JOIN (
            		    SELECT HFM_STAFFNO, MAX(CONCAT(HFM_ACTION_DATE, ' ', HFM_ACTION_TIME)) AS latest
            		    FROM HR_FILE_ACTIONS
            		    GROUP BY HFM_STAFFNO
            		) latest_actions
            		ON CONCAT(a.HFM_ACTION_DATE, ' ', a.HFM_ACTION_TIME) = latest_actions.latest
            		AND a.HFM_STAFFNO = latest_actions.HFM_STAFFNO
            		JOIN HR_FILE_MANAGEMENT m ON a.HFM_STAFFNO = m.HFM_STAFFNO
            		WHERE a.HFM_ACTION_TYPE = 'Issue'
            		""";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Issued Files Report</title>");
            out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");
            out.println("</head>");
            out.println("<body>");

            out.println("<a href='index.jsp'><button class='back-btn'>Back</button></a>");
            out.println("<h2>Currently Issued Files</h2>");


            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<table class='reports-table'>");
                out.println("<tr><th>Staff No</th><th>Name:</th><th>Action</th><th>Person</th><th>Date</th><th>Time</th></tr>");
                out.println("<tr>");
                out.println("<td>" + rs.getString("HFM_STAFFNO") + "</td>");
                out.println("<td>" + rs.getString("HFM_NAME") + "</td>");
                out.println("<td>" + rs.getString("HFM_ACTION_TYPE") + "</td>");
                out.println("<td>" + rs.getString("HFM_PERSON") + "</td>");
                out.println("<td>" + rs.getString("HFM_ACTION_DATE") + "</td>");
                out.println("<td>" + rs.getString("HFM_ACTION_TIME") + "</td>");
                out.println("</tr>");
                out.println("</table>");
            }

            if (!found) {
                out.println("<p>No files are currently issued.</p>");
            }


            out.println("</body></html>");

            con.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }
}
