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

            String sql = "SELECT * FROM HR_FILE_ACTIONS WHERE HFM_ACTION = 'Issue'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            out.println("<html><head><title>Issued Files Report</title>");
            out.println("<link rel='stylesheet' href='css/styles.css'>");
            out.println("</head><body>");

            out.println("<a href='index.jsp'><button class='back-btn'>Back</button></a>");
            out.println("<h2>Issued Files Report</h2>");

            out.println("<table border='1'>");
            out.println("<tr><th>Staff No</th><th>Name</th><th>Action</th><th>Person</th><th>Date</th><th>Time</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("HFM_STAFFNO") + "</td>");
                out.println("<td>" + rs.getString("HFM_NAME") + "</td>");
                out.println("<td>" + rs.getString("HFM_ACTION") + "</td>");
                out.println("<td>" + rs.getString("HFM_PERSON") + "</td>");
                out.println("<td>" + rs.getString("HFM_DATE") + "</td>");
                out.println("<td>" + rs.getString("HFM_TIME") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

            con.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
