package com.fms;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/add")
public class AddEmployee extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String staffNo = request.getParameter("staff_no");
        String name = request.getParameter("name");
        String active = request.getParameter("active");
        String rackNumber = request.getParameter("rack_number");
        String remarks = request.getParameter("remarks");
        String employeeGroup = request.getParameter("employee_group");
        String status = request.getParameter("status");  // New field

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/staff", "root", "hope");

            String sql = "INSERT INTO HR_FILE_MANAGEMENT (HFM_STAFFNO, HFM_NAME, HFM_ACTIVE, HFM_RACK_NO, HFM_REMARKS, HFM_EMPLOYEE_GROUP, HFM_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);            
            ps.setString(1, staffNo);
            ps.setString(2, name);
            ps.setString(3, active);
            ps.setString(4, rackNumber);
            ps.setString(5, remarks);
            ps.setString(6, employeeGroup);
            ps.setString(7, status); 

            int result = ps.executeUpdate();

            out.println("<html><head><link rel='stylesheet' href='css/styles.css'></head><body>");
            if (result > 0) {
                out.println("<div class='confirmation'>File details of the employee added successfully!</div>");
            } else {
                out.println("<div class='confirmation error'>Failed to add employee.<br>There was a problem adding the file. Please try again.</div>");
            }
            out.println("<a href='index.jsp'><button class='back-btn'>");
            out.println("<svg xmlns='http://www.w3.org/2000/svg' width='20' height='20' fill='currentColor' class='bi bi-arrow-left-circle' viewBox='0 0 16 16'>");
            out.println("<path fill-rule='evenodd' d='M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z'/>");
            out.println("</svg></button></a>");
            out.println("</body></html>");

            con.close();
        } catch (Exception e) {
        	out.println("<a href='index.jsp'><button> Back to search </button></a>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }
}
