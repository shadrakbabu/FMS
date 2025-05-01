package com.fms;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/update")
public class UpdateEmployee extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String staffNo = request.getParameter("staff_no");
        String name = request.getParameter("name");
        String rackNumber = request.getParameter("rack_number");
        String active = request.getParameter("active");
        String employeeGroup = request.getParameter("employee_group");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/staff", "root", "hope");

            String sql = "UPDATE HR_FILE_MANAGEMENT SET HFM_NAME = ?, HFM_RACK_NO = ?, HFM_ACTIVE = ?, HFM_EMPLOYEE_GROUP = ?, HFM_REMARKS = ? WHERE HFM_STAFFNO = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, rackNumber);
            ps.setString(3, active);
            ps.setString(4, employeeGroup);
            ps.setString(5, staffNo);
            ps.setString(6, staffNo);
            
            int rowsUpdated = ps.executeUpdate();

            out.println("<html><body>");
            if (rowsUpdated > 0) {
                out.println("<p> File details update successfully. </p>");
            } else {
                out.println("<p>Update failed.</p>");
            }
            out.println("<a href='index.jsp'><button class='back-btn'>");
            out.println("  <svg xmlns='http://www.w3.org/2000/svg' width='20' height='20' fill='currentColor' class='bi bi-arrow-left-circle' viewBox='0 0 16 16'>");
            out.println("    <path fill-rule='evenodd' d='M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z'/>");
            out.println("  </svg>");
            out.println("</button></a>");

            out.println("</body></html>");

            con.close();

        } catch (Exception e) {
        	out.println("<a href='index.jsp'><button> Back to search </button></a>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }
}
