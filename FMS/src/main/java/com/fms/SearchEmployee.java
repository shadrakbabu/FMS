package com.fms;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/search")
public class SearchEmployee extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String query = request.getParameter("query");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/staff", "root", "hope");

            String sql = "SELECT * FROM HR_FILE_MANAGEMENT WHERE HFM_STAFFNO = ? OR HFM_NAME LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, query);
            ps.setString(2, "%" + query + "%");
            ResultSet rs = ps.executeQuery();

            out.println("<html><head>");
            out.println("<title>Search Results</title>");
            out.println("<link rel='stylesheet' type='text/css' href='css/styles.css'>");

            out.println("</head><body>");

            out.println("<a href='index.jsp'><button class='back-btn'>");
            out.println("<svg xmlns='http://www.w3.org/2000/svg' width='20' height='20' fill='currentColor' class='bi bi-arrow-left-circle' viewBox='0 0 16 16'>");
            out.println("<path fill-rule='evenodd' d='M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z'/>");
            out.println("</svg></button></a>");

            out.println("<h2>Search Results</h2>");

            boolean found = false;
            String staffNo = null;

            while (rs.next()) {
                found = true;
                staffNo = rs.getString("HFM_STAFFNO");

                // Fetch latest action (to show status)
                String latestPersonIssued = "Available"; // default
                PreparedStatement ps2 = con.prepareStatement("SELECT HFM_PERSON, HFM_ACTION_TYPE FROM HR_FILE_ACTIONS WHERE HFM_STAFFNO = ? ORDER BY HFM_ID DESC LIMIT 1");
                ps2.setString(1, staffNo);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    String actionType = rs2.getString("HFM_ACTION_TYPE");
                    String person = rs2.getString("HFM_PERSON");
                    if ("Issue".equalsIgnoreCase(actionType)) {
                        latestPersonIssued = person; // Last issued person
                    } else {
                        latestPersonIssued = "Available"; // Received back
                    }
                }

                // Employee Details Form
                out.println("<form method='post' action='update'>");
                out.println("<div class='form-group'><label>Staff No:</label><input type='text' name='staff_no' value='" + staffNo + "' readonly></div>");
                out.println("<div class='form-group'><label>Name:</label><input type='text' name='name' value='" + rs.getString("HFM_NAME") + "' readonly></div>");
                out.println("<div class='form-group'><label>Rack No:</label><input type='text' name='rack_number' value='" + rs.getString("HFM_RACK_NO") + "' readonly></div>");
                out.println("<div class='form-group'><label>Active:</label><input type='text' name='active' value='" + rs.getString("HFM_ACTIVE") + "' readonly></div>");
                out.println("<div class='form-group'><label>Employee Group:</label><input type='text' name='employee_group' value='" + rs.getString("HFM_EMPLOYEE_GROUP") + "' readonly></div>");
                out.println("<div class='form-group'><label>Remarks:</label><input type='text' name='remarks' value='" + rs.getString("HFM_REMARKS") + "' readonly></div>");

                // New Status Field (Latest issued to OR Available)
                out.println("<div class='form-group'><label>Status (Last Issued To):</label><input type='text' name='status' value='" + latestPersonIssued + "' readonly></div>");

                out.println("<br>");
                out.println("<button type='button' onclick='enableEdit()'>Edit</button>");
                out.println("<input type='submit' id='saveBtn' value='Save' style='display:none;'>");
                out.println("</form>");
            }

            if (!found) {
                out.println("<p>No staff number or name exists.</p>");
            } else {
                // Action Form
            	out.println("<h3>Action :</h3>");
            	out.println("<form action='actionSubmit' method='post' class='action-form'>");
            	out.println("<input type='hidden' name='staff_no' value='" + staffNo + "'>");
            	out.println("<div class='form-group'><label>Action:</label>");
            	out.println("<select name='action_type' id='actionSelect' required onchange='showPersonSelect(this)'>");
            	out.println("<option value=''>   Select Action   </option>");
            	out.println("<option value='Issue'>Issue</option>");
            	out.println("<option value='Receive'>Receive</option>");
            	out.println("</select></div>");
            	out.println("<div class='form-group' id='personGroup' style='display:none;'>");
            	out.println("<label>Person:</label>");
            	out.println("<select name='person_name' onchange='togglePersonInput(this)'>");
            	out.println("<option value=''>-- Select Person --</option>");
            	out.println("<option value='Shantanu Bhowmick'>Shantanu Bhowmick</option>");
            	out.println("<option value='Jaya Prakasam'>Jaya Prakasam</option>");
            	out.println("<option value='Md Khaleel'>Md Khaleel</option>");
            	out.println("<option value='S Leela'>S Leela</option>");
            	out.println("<option value='Chandra Sekhar'>Chandra Sekhar</option>");
            	out.println("<option value='other'>other</option>");
            	out.println("</select></div>");
                out.println("<div class='form-group' id='otherPersonGroup' style='display:none;'>");
                out.println("  <label>Enter Person Name:</label>");
                out.println("  <input type='text' id='other_person' placeholder='Enter name here'>");
                out.println("</div>");
            	out.println("<div class='form-group'><input type='submit' value='Submit Action'></div>");
            	out.println("</form>");


                // Action History Table
                out.println("<h3>Action History</h3>");
                out.println("<table class='action-table'><thead><tr><th>Action</th><th>Person</th><th>Date</th><th>Time</th></tr></thead><tbody>");

                PreparedStatement ps3 = con.prepareStatement("SELECT * FROM HR_FILE_ACTIONS WHERE HFM_STAFFNO = ? ORDER BY HFM_ID DESC");
                ps3.setString(1, staffNo);
                ResultSet rs3 = ps3.executeQuery();
                while (rs3.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs3.getString("HFM_ACTION_TYPE") + "</td>");
                    out.println("<td>" + rs3.getString("HFM_PERSON") + "</td>");
                    out.println("<td>" + rs3.getDate("HFM_ACTION_DATE") + "</td>");
                    out.println("<td>" + rs3.getTime("HFM_ACTION_TIME") + "</td>");
                    out.println("</tr>");
                }

                out.println("</tbody></table>");
            }

            
            out.println("<script>");
            out.println("function enableEdit() {");
            out.println("  let inputs = document.querySelectorAll('form input[type=\"text\"]');");
            out.println("  inputs.forEach(function(input) {");
            out.println("    if (input.name !== 'status') {");
            out.println("      input.removeAttribute('readonly');");
            out.println("    }");
            out.println("  });");
            out.println("  document.getElementById('saveBtn').style.display = 'inline-block';");
            out.println("}");

            out.println("function showPersonSelect(select) {");
            out.println("  var personGroup = document.getElementById('personGroup');");
            out.println("  var otherPersonGroup = document.getElementById('otherPersonGroup');");
            out.println("  if (select.value === 'Issue') {");
            out.println("    personGroup.style.display = 'block';");
            out.println("  } else {");
            out.println("    personGroup.style.display = 'none';");
            out.println("    otherPersonGroup.style.display = 'none';");
            out.println("  }");
            out.println("}");

            out.println("function togglePersonInput(select) {");
            out.println("  var otherGroup = document.getElementById('otherPersonGroup');");
            out.println("  if (select.value === 'other') {");
            out.println("    otherGroup.style.display = 'block';");
            out.println("  } else {");
            out.println("    otherGroup.style.display = 'none';");
            out.println("  }");
            out.println("}");

            out.println("document.addEventListener('DOMContentLoaded', function () {");
            out.println("  const form = document.querySelector('.action-form');");
            out.println("  if (form) {");
            out.println("    form.addEventListener('submit', function (e) {");
            out.println("      const personSelect = form.querySelector('select[name=\"person_name\"]');");
            out.println("      const otherInput = form.querySelector('input[name=\"other_person\"]');");
            out.println("      if (personSelect && otherInput && personSelect.value === 'other') {");
            out.println("        personSelect.value = otherInput.value.trim();");
            out.println("      }");
            out.println("    });");
            out.println("  }");
            out.println("});");
            out.println("</script>");          

            out.println("</body></html>");
            con.close();
  
            
            
            

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }
}
