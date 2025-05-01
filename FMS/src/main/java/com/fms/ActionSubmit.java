package com.fms;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/actionSubmit")
public class ActionSubmit extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String staffNo = request.getParameter("staff_no");
        String actionType = request.getParameter("action_type");
        String personName = request.getParameter("person_name");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/staff", "root", "hope");

            String sql = "INSERT INTO HR_FILE_ACTIONS (HFM_STAFFNO, HFM_ACTION_TYPE, HFM_PERSON, HFM_ACTION_DATE, HFM_ACTION_TIME) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, staffNo);
            ps.setString(2, actionType);
            ps.setString(3, personName);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            ps.setTime(5, java.sql.Time.valueOf(LocalTime.now()));
            ps.executeUpdate();
            con.close();

            response.sendRedirect("search?query=" + staffNo); // Redirect back to search page
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }
}
