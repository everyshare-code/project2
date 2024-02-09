<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="util" uri="/WEB-INF/tlds/boardtaglib.tld" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="<c:url value="${util:getPath('LOGIN-CSS') }"/>" rel="stylesheet" />
    <link rel="icon" href="<c:url value="${util:getPath('IMG-FAVICON') }"/>" type="image/x-icon">
    <script src="<c:url value="${util:getPath('LOGIN-JS')}"/>?ver=1.1"></script>
    <title>로그인</title>
</head>
<body>
<div class="container-sm center-vertical">
	
    <form class="custom-form" action="<c:url value="${util:getPath('LOGIN') }"/>" method="post">
    	<input type="hidden" id="login-status" value="${not empty member ? 1:0}"/>
    	<div class="mb-3" id="div-logo">
            <a href="">
                <img id="logo" class="form-control" src="<c:url value="${util:getPath('IMG-LOGO') }"/>"/>
            </a>
            <figure class="text-end">
                <a href="<c:url value="${util:getPath('JOIN') }"/>" id="join-btn">회원가입을 하시겠어요?</a>
            </figure>
        </div>
        
        <div class="mb-3">
            <input type="text" class="form-control" id="username" name="username" placeholder="아이디">
        </div>
        <div class="mb-3">
        	<input type="text" class="form-control" id="password-h" placeholder="비밀번호">
            <input type="password" class="form-control" id="password" name="password" hidden>
        </div>
        <button type="submit" class="form-control btn btn-primary" id="login-submit">로그인</button>
        
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<c:if test="${!empty LOGIN_ERROR }">
	<script>
		alert('${LOGIN_ERROR}');
	</script>
</c:if>
</body>
</html>
