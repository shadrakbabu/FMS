<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>FMS - Find Files</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <h2>File Management System</h2>

    <form action="search" method="get">
        <div class="form-group">
            <label>Enter Staff No or Name:</label>
            <input type="text" name="query" required>
        </div>
        <div class="button-row">
            <input type="submit" value="Search">
        </div>
    </form>

    <a href="add.jsp">
        <button class="add-btn">+ Add New File</button>
    </a>
</body>
<!-- Designed & Developed by Shadrak Babu -->
</html>
