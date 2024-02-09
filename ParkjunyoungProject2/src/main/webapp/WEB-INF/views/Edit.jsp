<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="util" uri="/WEB-INF/tlds/boardtaglib.tld" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <link href="<c:url value="${util:getPath('JOIN-CSS') }"/>?ver=1.3" rel="stylesheet"/>
    <script src="<c:url value="${util:getPath('EDIT-JS') }"/>?ver=1.5"></script>
    <link rel="icon" href="<c:url value="${util:getPath('IMG-FAVICON') }"/>" type="image/x-icon">
    <title>나의 정보 수정</title>
</head>
<body>
    <div class="container-sm center-vertical">
        <form class="custom-form w-auto p-3" action="<c:url value="${util:getPath('EDIT') }"/>" method="post" enctype="multipart/form-data">
            <input type="hidden" id="checkURL" value="<c:url value="${util:getPath('CHECK-ID') }"/>"/>
            <input type="hidden" id="savedFile" name="savedFile" value="${member.self_image }"/>
            <input type="hidden" name="encoded-file" id="encoded-file" />
            
            <input type="file" id="file-input" name="input-file" class="d-none" accept="image/*" >
            
            <div class="mb-3 text-center" id="self-image-div">
            	<button type="button" class="btn-close" aria-label="Close"></button>
                <a href="javascript:void(0)" class="img-fluid">
                    <img id="self-image" class="img-thumbnail rounded-circle" src="${member.self_image}"/>
                </a>
            </div>
            
            <div class="form-text username-msg-container">
              <span class="container d-flex justify-content-center" id="file-warning">${empty member.self_image?'프로필 이미지를 등록하려면 이미지를 클릭':'' }</span>
            </div>
            <div class="input-group mb-3">
            <span class="input-group-text">아이디</span>
            <input type="text" id="username" name="username" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" maxlength="10" value="${!empty param.username? param.username:member.username}" readonly>
            </div>
            <div class="form-text username-msg-container">
                <span id="usernameMsg"></span>
            </div>
            <div class="input-group mb-3">
            <span class="input-group-text">비밀번호</span>
            <input type="password" name="password" id="password" value="${!empty param.password?param.password:member.password }" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" maxlength="10">
            </div>
            <div class="form-text username-msg-container">
                <span id="passwordMsg">비밀번호는 영문,숫자,특수문자를 포함한 8자 이상</span>
            </div>
            <div class="input-group">
              <div class="d-flex me-4">
                  <label class="input-group-text me-3">성별</label>
              </div>
              <div class="form-check-inline d-flex justify-content-center">
                <div class="form-check me-3">
                    <input class="form-check-input" type="radio" name="gender" id="gender-m" value="M" disabled <c:if test="${member.gender=='M'||param.gender=='M'}">checked</c:if>/>
                    <label class="form-check-label" for="gender-m">남자</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="gender" id="gender-f" value="F" disabled <c:if test="${member.gender=='F'||param.gender=='F'}">checked</c:if>>
                    <label class="form-check-label" for="gender-f">여자</label>
                </div>
            </div>
          </div>
          
            <div class="form-text username-msg-container">
                <span></span>
            </div>
            <div class="input-group mb-3">
                <div class="d-flex align-items-center justify-content-between">
                  <span class="input-group-text me-3">관심사항</span>
                </div>
                <div class="d-flex inters-container">
                  <div class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="inters" value="POL" <c:if test="${fn:contains(member.inters,'POL')||fn:contains(param.inters,'POL')}">checked</c:if>>
                    <label class="form-check-label" for="inters1">정치</label>
                  </div>
                  <div class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="inters" value="ECO" <c:if test="${fn:contains(member.inters,'ECO')||fn:contains(param.inters,'POL')}">checked</c:if>>
                    <label class="form-check-label" for="inters2">경제</label>
                  </div>
                  <div class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="inters" value="ENT" <c:if test="${fn:contains(member.inters,'ENT')||fn:contains(param.inters,'POL')}">checked</c:if>>
                    <label class="form-check-label" for="inters3">연예</label>
                  </div>
                </div>
              </div>
              <div class="form-text username-msg-container">
                <span id="intersMsg">관심사항은 하나 이상 선택</span>
            </div>
            <div class="input-group mb-1">
              <div class="d-flex w-100 align-items-center">
                  <span class="input-group-text me-3">학력</span>
                  <select class="form-select w-100" id="education" name="education">
                      <option>학력사항을 선택해주세요</option>
                      <option <c:if test="${member.education=='EL'||param.education=='EL' }">selected</c:if> value="EL">초등학교</option>
                      <option <c:if test="${member.education=='MI'||param.education=='MI' }">selected</c:if> value="MI">중학교</option>
                      <option <c:if test="${member.education=='HI'||param.education=='HI' }">selected</c:if> value="HI">고등학교</option>
                      <option <c:if test="${member.education=='CO'||param.education=='CO' }">selected</c:if> value="CO">대학교</option>
                  </select>
              </div>
          </div>
              <div class="form-text username-msg-container">
                <span id="educationMsg"></span>
            </div>
            <div class="form-floating mt-2">
                <textarea class="form-control" id="self-intro" name="self-intro">${not empty member.self_intro?member.self_intro:param['self-intro'] }</textarea>
                <c:if test="${empty member.self_intro && empty param['self-intro'] }"><label class="form-text" for="self-intro">자기소개를 입력해주세요</label></c:if>
              </div>
              <div class="form-text username-msg-container">
                <span></span>
            </div>
            <button type="submit" class="form-control btn btn-primary" id="join-submit">수정하기</button>
        </form>
    </div>
</body>
</html>
