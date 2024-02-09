<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="util" uri="/WEB-INF/tlds/boardtaglib.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<jsp:include page="${util:getPath('TOP-VIEW') }"/>

   <div data-bs-spy="scroll" data-bs-target="card-container" data-bs-offset="0" class="content-wrapper" tabindex="0">
        <div class="card-container text-center g-0" id="card-container">
          <!--입력 폼-->
          <div class="card text-muted mt-3" id="write-form">
            <div class="card-header d-flex justify-content-between align-items-center">
                <div class="card-header-profile-div">
                  <a class="navbar-brand" href="javascript:void(0);" data-value="${member.username }">
                      <img src="${not empty member.self_image?member.self_image:util:getPath('IMG-PROFILE-BOARD') }" alt="profile-basic-board" width="30" height="30" class="d-inline-block rounded-circle">
                      <span class="mr-4">${member.username }</span>
                  </a>
              </div>
                <div class="card-header-postdate align-middle d-flex">
                	<a href="javascript:void(0);" class="btn d-inline" id="btn-image">
                    	<img src="<c:url value="${util:getPath('IMG-UPLOAD') }"/>" class="d-inline-block" alt="img-upload"/>
                    </a>
                    
                	<a href="javascript:void(0);" class="btn d-inline" id="btn-write">
                    	<img src="<c:url value="${util:getPath('IMG-WRITE') }"/>" class="d-inline-block" alt="img-write"/>
                    </a>
                </div>
            </div>
            <form id="bbs-form">
                <div class="card-body">
                  <div id="image-container" class="image-container text-center custom-dashed-border">
                       		<div class="row image-upload-label">
	                           	<div class="d-flex">
	                               	<img id="image-display" src="" class="d-inline-block d-none" alt="img-upload" />
	                            </div>
	                        </div>
		                    <input type="hidden" id="bbs-encoded-file-write" name="bbs-encoded-file-write"/>
		                    <input type="file" id="image-upload" class="d-none" accept="image/*"/>
	                </div>
                    <div class="d-flex">
					  <!-- 텍스트를 입력할 수 있는 input -->
					  <textarea class="form-control text-muted rounded-bottom card-textarea" placeholder="내용 입력" id="bbs-content-write" name="bbs-content-write"></textarea>
					</div>
                    </div>
                  </form>
             </div>
            
            <!--입력 폼-->
            <div class="card text-muted mt-3 text-center py-3 d-none" id="empty-container">
			  <div class="card-body">
			    <p class="p-6 text-muted mb-0">검색 결과가 없어요</p>
			    </div>
			</div>
			<!-- 게시글 폼 끝 -->
			<div class="card text-muted mt-3 bbs-container d-none" id="bbs-container">
            <div class="card-header d-flex justify-content-between">
              <div class="card-header-profile-div">
                  <a class="navbar-brand" href="#" role="button" data-toggle="modal" data-target="#viewModal" >	
                      <img id="bbs-profile" src="<c:url value="${util:getPath('IMG-PROFILE-BOARD') }"/>" alt="profile-basic-board" width="30" height="30" class="d-inline-block">
                      <span class="mr-4" id="bbs-username">a1234</span>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <small id="bbs-postdate">2023-12-11</small>
                  </a>
              </div>
              <div>
              	<div class="card-header-postdate align-middle d-flex">
                	<a href="javascript:void(0);" class="btn d-inline d-none" id="bbs-btn-image">
                    	<img src="<c:url value="${util:getPath('IMG-UPLOAD') }"/>" class="d-inline-block" alt="img-upload"/>
                    </a>
                    
                	<a href="javascript:void(0);" class="btn d-inline d-none" id="bbs-btn-write">
                    	<img src="<c:url value="${util:getPath('IMG-WRITE') }"/>" class="d-inline-block" alt="img-write"/>
                    </a>
                    <a class="nav-link navbar-brand" href="#" role="button" data-toggle="modal" data-target="#bottomModal">
				      <img class="profile-img object-fit-cover" src="<c:url value="${util:getPath('IMG-MORE') }"/>" id="bbs-btn-more" alt="Profile"/>
			    </a>
                </div>
                
			    <input type="hidden" id="bbs-no" value=""/>
		 	</div>
            </div>
            
          <div class="card-body">
            <div id="bbs-img-container">
            <input type="hidden" id="bbs-saved-file">
            <input type="hidden" id="bbs-encoded-file" name="bbs-encoded-file">
            <input type="file" id="bbs-input-file" class="d-none" accept="image/*">
            </div>
            
            <textarea class="form-control text-muted rounded-bottom card-textarea d-none" placeholder="내용 입력" id="bbs-content-update" name="bbs-content-update"></textarea>
             <div class="mt-3 px-3" id="bbs-content-one">
            	<!-- <p class="lead card-text">내용을 입력하세요</p> -->
            </div>
             <div class="accordion" id="bbs-content-container">
			    <div>
			      <div id="bbs-collapseOne" class="collapse" aria-labelledby="bbs-content-more" data-parent="#bbs-content-container">
			        <div class="card-body">
			          <div class="mt-1 px-3" id="bbs-content-all">
			          </div>
			        </div>
			      </div>
			      <div id="bbs-content-more" class="py-2">
			        <h2 class="mb-0">
			          <a href="#" role="button" id="more-link" class="btn-link text-muted d-block px-3" type="button" data-toggle="collapse" data-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
			            더보기
			          </a>
			        </h2>
			      </div>
			    </div>
			  </div>
              <!-- <div class="mt-3 card-body-tag-container">
                <a href="#" class="btn btn-sm card-body-tag" tabindex="-1" role="button" aria-disabled="true">검색</a>
                <a href="#" class="btn btn-sm card-body-tag" tabindex="-1" role="button" aria-disabled="true">태그</a>
                <a href="#" class="btn btn-sm card-body-tag" tabindex="-1" role="button" aria-disabled="true">설정</a>
                <a href="#" class="btn btn-sm card-body-tag" tabindex="-1" role="button" aria-disabled="true">정보</a>
                <a href="#" class="btn btn-sm card-body-tag" tabindex="-1" role="button" aria-disabled="true">제공</a>
              </div> -->
              <div class="card-body-status py-3 px-4" id="bbs-like-container">
              	<a class="navbar-brand" href="javascript:void(0)" id="bbs-like-btn">
                  <img class="profile-img card-like" src="<c:url value="${util:getPath('IMG-LIKE-LINE') }"/>"/>
                  <span class="card-span" id="bbs-like">0</span>
               </a>
              </div>
            </div>
              <div class="card-footer" id="bbs-footer-container">
                <div class="row text-center">
                  <div class="col">
                    <a class="card-emoticon-hover" href="#">
                      <img class="profile-img card-emoticon" src="<c:url value="${util:getPath('IMG-COMMENT') }"/>"/>
                    </a>
                  </div>
                  <div class="col">
                    <a class="card-emoticon-hover" href="#">
                      <img class="profile-img card-emoticon" src="<c:url value="${util:getPath('IMG-SEND') }"/>"/>
                    </a>
                  </div>
                </div>
              </div>
            </div>
			
            
              </div>
          </div><!--card-container-->
        </div><!--scrollpane-->
   <!-- bottomModal -->
  <div class="modal fade mt-auto" id="bottomModal" tabindex="-1" role="dialog" aria-labelledby="bottomModalLabel" aria-hidden="true">
    <div class="modal-dialog fixed-bottom" role="document">
      <div class="modal-content">
          <button type="button" id="btn-update" class="btn-more btn rounded-top" data-dismiss="modal" aria-label="Close">수정</button>
     	<button type="button" id="btn-delete" class="btn-more btn rounded-top" data-dismiss="modal" aria-label="Close">삭제</button>
          <button type="button" id="btn-close" class="btn-more btn rounded-bottom" data-dismiss="modal" aria-label="Close">닫기</button>
      </div>
    </div>
  </div>
  
<jsp:include page="${util:getPath('FOOTER-VIEW') }"/>
