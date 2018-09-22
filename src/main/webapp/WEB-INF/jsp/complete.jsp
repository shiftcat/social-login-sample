<%--
  Created by IntelliJ IDEA.
  User: yhlee
  Date: 18. 9. 21
  Time: 오전 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Complete</title>
</head>
<body>
    <p>User Infomation</p>
    ${user.userIdx} <br/>
    ${user.userName} <br/>
    ${user.userEmail} <br/>
    ${user.userImage} <br/>
    ${user.socialType} <br/>
    ${user.createDate} <br/>

    <a href="/logout">Logout</a> <br/>

    <c:import url="menu.jsp"/>

</body>
</html>
