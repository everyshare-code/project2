<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="util" uri="/WEB-INF/tlds/boardtaglib.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>    

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Montserrat+Alternates:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link rel="icon" href="<c:url value="${util:getPath('IMG-FAVICON') }"/>" type="image/x-icon">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
    <script  src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.bundle.min.js"></script>
	<link rel="stylesheet" href="<c:url value="${util:getPath('TOP-CSS') }"/>?ver=1.8">
	<link rel="stylesheet" href="<c:url value="${util:getPath('INDEX-CSS') }"/>?ver=1.8">
	<script src="<c:url value="${util:getPath('INDEX-JS') }"/>?ver=1.4"></script>
    <title>Board</title>
</head>
<body>
<input type="hidden" id="username" value="${member.username }"/>
 <!-- 상단 네비게이션 바 (PC 화면) -->
    <div class="navbar-wrapper d-none d-md-block">
        <nav class="navbar navbar-expand-md navbar-light bg-light">
            <div class="container-fluid d-flex justify-content-between">
                <a class="navbar-brand nav-logo" href="<c:url value="${util:getPath('INDEX') }"/>">
                    <img src="<c:url value="${util:getPath('IMG-LOGO-SMALL') }"/>" alt="" class="d-inline-block nav-logo-img">
                </a>
           	  	<input type="text" class="search-click" name="searchWord" placeholder="검색" />
                <c:if test="${empty member }" var="notLogin">
                	<a class="nav-link" href="<c:url value="${util:getPath('LOGIN') }"/>" id="login-btn">로그인 / 회원가입</a>
                </c:if>
                <c:if test="${!notLogin}">
                <div class="btn-group">
					<a class="nav-link nav-profile navbar-brand " href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
				      <img class="rounded-circle profile-img object-fit-cover" src="${not empty member.self_image?member.self_image:util:getPath('IMG-PROFILE-BOARD') }" id="profile-mini" alt="Profile"/>
				    </a>
				    
				    <ul class="dropdown-menu dropdown-menu-end">
					    <li><button class="dropdown-item fs-6" type="button" data-toggle="modal" data-target="#viewModal" data-value="${member.username}">나의 정보 보기</button>
					    </li>
					    <li><a class="dropdown-item fs-6" role="button" href="<c:url value="${util:getPath('LOGOUT') }"/>">로그아웃</a></li>
					 </ul>
    			</div>
			    </c:if>
            </div>
        </nav>
    </div>
    
<!-- 하단 네비게이션 바 (모바일 화면) -->
<div class="navbar-wrapper d-md-none">
    <nav class="navbar navbar-expand-md navbar-light bg-light fixed-bottom">
        <div class="container-fluid d-flex justify-content-between">
            <input type="text" class="search-click" name="searchWord" placeholder="검색" />
            <!-- 로고 -->
            <a class="navbar-brand nav-logo m-auto" href="#" role="button">
                <img src="<c:url value="${util:getPath('IMG-LOGO-SMALL') }"/>" alt="" class="d-inline-block nav-logo-img">
            </a>
            
            <!-- 프로필 이미지 -->
            <c:if test="${!empty member}" var="login">
                <div class="btn-group dropup">
					<a class="nav-link nav-profile navbar-brand" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
				      <img class="rounded-circle profile-img object-fit-cover" src="${not empty member.self_image?member.self_image:util:getPath('IMG-PROFILE-BOARD') }" id="profile-mini" alt="Profile"/>
				    </a>
				    <ul class="dropdown-menu dropdown-menu-end">
					    <li><button class="dropdown-item fs-6" type="button" data-toggle="modal" data-target="#viewModal" data-value="${member.username}">나의 정보 보기</button>
					    </li>
					    <li><a class="dropdown-item fs-6" role="button" href="<c:url value="${util:getPath('LOGOUT') }"/>">로그아웃</a></li>
					 </ul>
    			</div>
            </c:if>
            <c:if test="${not login }">
            	<a class="nav-link" href="<c:url value="${util:getPath('LOGIN') }"/>" id="login-btn">로그인 / 회원가입</a>
            </c:if>
        </div>
    </nav>
</div>
<div class="modal fade row py-5 px-4 modal-dialog-scrollable" id="viewModal" tabindex="-2" role="dialog" aria-labelledby="viewModalLabel" aria-hidden="true">
	<div class="col-md-5 mx-auto modal-dialog">
        <div class="bg-white shadow rounded overflow-hidden modal-content">
            <div id="profileCover" class="px-4 pt-0 pb-4 cover">
                <div class="media align-items-end profile-head d-flex">
                    <div class="profile mr-3 d-flex flex-column">
                        <img id="self_image" src="" alt="..." width="130" class="rounded-circle mb-2 img-thumbnail profile-img">
                     <a href="" class="btn btn-sm btn-block" id="udtBtn">나의 정보 수정</a>
                    </div>
                    <div class="px-3 media-body mb-5 text-white">
                        <h4 class="mt-0 mb-0">
                        	<img id="gender" src=""/>
                        	<span id="username-modal"></span>
                        </h4>
                        <p id="joindate" class="small mb-4">
                            <i class="fas fa-map-marker-alt mr-2"></i>
                        </p>
                    </div>
                </div>
            </div>
            <div class="bg-light p-4 d-flex justify-content-end text-center">
                <ul class="list-inline mb-0">
                    <li class="list-inline-item">
                        <h5 id="like-count" class="font-weight-bold mb-0 d-block text-reset f-black">215</h5>
                        <small class="text-muted">
                            <i class="fas fa-image mr-1"></i>좋아요
                        </small>
                    </li>
                    <li class="list-inline-item">
                        <h5 id="board-count" class="font-weight-bold mb-0 d-block text-reset f-black">215</h5>
                        <small class="text-muted">
                            <i class="fas fa-image mr-1"></i>작성글
                        </small>
                    </li>
                </ul>
            </div>
            <div class="px-4 py-3">
            
                
                <h6 class="mt-3 mb-2 text-muted fw-bolder">관심사항</h6>
                <div>
               		<h4 class="d-flex" id="inters">
               		
               		</h4>
                </div>
                <h6 class="mt-3 mb-2 text-muted fw-bolder">학력</h6>
              		<h4>
              		<span class="badge bg-secondary fs-6" id="education">
              		</span>
              		
              		</h4>
                
                <h6 class="mb-2 text-muted fw-bolder">자기소개</h6>
                <div class="p-4 rounded shadow-sm bg-light">
	                	<p id="self_intro" class="mb-0"></p>
                </div>
            </div>
            <div class="justify-content-center py-4 px-4">
                <div class="d-flex align-items-center justify-content-between mb-3 px-3">
                    <h6 class="mb-0 text-muted fw-bolder">최근 게시글</h6>
                    <a href="javascript:setUser()" class="btn btn-link text-muted" id="modal-more" data-dismiss="modal">더보기</a>
                </div>
                <div class="container row align-items-center" id="user-bbs">
                    <div class="col-lg-6 mb-2 pr-lg-1">
                        <div class="content img-fluid rounded shadow-sm">
                        	<p class="lead text-muted fs-6">글 내용</p>
                        </div>
                    </div>
                    <div class="col-lg-6 mb-2 pl-lg-1">
                        <img src="https://images.unsplash.com/photo-1493571716545-b559a19edd14?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80" alt="" class="img-fluid rounded shadow-sm">
                    </div>
                    <div class="col-lg-6 pr-lg-1 mb-2">
                        <img src="https://images.unsplash.com/photo-1453791052107-5c843da62d97?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80" alt="" class="img-fluid rounded shadow-sm">
                    </div>
                    <div class="col-lg-6 pl-lg-1">
                        <img src="https://images.unsplash.com/photo-1475724017904-b712052c192a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80" alt="" class="img-fluid rounded shadow-sm">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


  
  <%-- <div class="profile-container align-items-center justify-content-center">
   <a id="join-btn" class="nav-profile nav-link d-inline-block mr-2" href="">
    	회원가입/로그인
    </a>  
    
     <a class="navbar-brand nav-logo ms-auto" href="#">
      <img src="<c:url value="${util:getPath('IMG-SEARCH') }"/>" alt="" class="d-inline-block">
  	</a>
  	
    
    
  <ul class="dropdown-menu dropdown-menu-end" id="dropdown">
      <li><a class="dropdown-item" href="#scrollspyHeading3">나의 정보</a></li>
      <li><a class="dropdown-item" href="#scrollspyHeading4">내가 작성한 글</a></li>
      <li><hr class="dropdown-divider"></li>
      <li><a class="dropdown-item" href="#scrollspyHeading5">로그아웃</a></li>
  </ul>
</div> --%>


