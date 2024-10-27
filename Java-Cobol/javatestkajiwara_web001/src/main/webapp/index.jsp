<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Remote Process Test</title>
</head>
<body>
	<h1>SSHTest</h1>


    <form action="kajiwaraServlet" method="post">
		
	  <h3>Option</h3>
	  <label>
	    <input type="radio" name="option" value="1" checked> Sync(JCL Success)
	  </label><br>
	  
	  <label>
	    <input type="radio" name="option" value="2"> Sync(JCL Error)
	  </label><br>
	  
	  <label>
	    <input type="radio" name="option" value="3"> Async(JCL Success)
	  </label><br>
	  
	  <label>
	    <input type="radio" name="option" value="4"> Async(JCL Error)
	  </label><br>
	  <br>
        <input type="submit" value="SSHTest">

    </form>
       
    
</body>
</html>