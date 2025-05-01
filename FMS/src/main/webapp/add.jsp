<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New File</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>

    <a href="index.jsp">
        <button class="back-btn">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" 
                 class="bi bi-arrow-left-circle" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8
                m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707
                l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5
                0 1 1 .708.708L5.707 7.5z"/>
            </svg>
        </button>
    </a>

    <h2>Add New Employee File</h2>

    <form action="add" method="post">

        <div class="form-group">
            <label for="staff_no">Staff No:</label>
            <input type="text" name="staff_no" id="staff_no" required>
        </div>

        <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" name="name" id="name" required>
        </div>

        <div class="form-group">
            <label for="rack_number">Rack No:</label>
            <input type="text" name="rack_number" id="rack_number">
        </div>

        <div class="form-group">
            <label for="available">Status:</label>
            <input type="text" name="available" id="available">
        </div>

        <div class="form-group">
            <label for="status">Active:</label>
            <input type="text" name="status" id="status">
        </div>

        <div class="form-group">
            <label for="employee_group">Employee Group:</label>
            <input type="text" name="employee_group" id="employee_group">
        </div>

        <div class="form-group">
            <label for="remarks">Remarks:</label>
            <input type="text" name="remarks" id="remarks">
        </div>

        <input type="submit" value="Save">
    </form>

</body>
</html>
