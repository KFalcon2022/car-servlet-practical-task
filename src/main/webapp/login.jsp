<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page session="false" %>
<html>
<head>
    <title>Login form</title>
</head>
<body>
<%
    if (response.getStatus() == 401) {
        out.println("<h1>Неверный логин или пароль!</h1>");
    }
%>
    <form action="./auth" method="post">
        Username: <input name="username"/>
        <br>
        <br>
        Password: <input name="password" type="password"/>
        <br>
        <br>
        <input type="submit" value="Log In"/>
    </form>
</body>
</html>
