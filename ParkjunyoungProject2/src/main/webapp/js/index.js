
window.history.pushState(null,'','');


	

document.addEventListener('DOMContentLoaded', function () {
	
		let cursor=0;
		let searchWord='';
		let searchUser='';
	  const username=document.querySelector('#username');
	  const writeForm=document.querySelector('#write-form');
	  
	  const searchInput=document.querySelectorAll('.search-click');
	  let cardContainers=[];
	  searchInput.forEach(element=>{
		  element.addEventListener('keypress',e=>{
			 if(e.keyCode===13){
				 if(e.target.value.trim()){
					 cardContainers.forEach(el=>{
						el.remove(); 
					 });
					 cardContainers=[];
					 searchWord=e.target.value;
					 searchUser='';
					 cursor=0;
					 e.target.value='';
					 e.target.blur();
					 selectBBS();
					 window.scrollTo(0,0);
				 }else{
					 alert('검색어를 입력하세요');
					 e.target.focus();
				 }
			 } 
		  });
	  })
	  
	  
	  //const cardContainer=document.querySelector('#card-container');
	  document.addEventListener('click',(e)=>{
		 if(!username.value){
			 if(e.target.nodeName!='A'){
				 alert('로그인 후 이용하세요');
				 location.replace('/login.do');
				 e.preventDefault();
				 return;
			 }
		 }
	  });
	  
	  
	  if(username.value){
		  //로그인 완료시 아이디 로컬스토리지 저장
		  localStorage.setItem("username",btoa(username.value));
		  //로그인시 뒤로가기 막기
		  history.pushState(null, null, location.href);
		    window.onpopstate = function () {
		        history.go(1);
		    };
//		  window.location.href='/index.do';
	  }else { //로그인 안할 경우 10초 뒤 페이지 이동
		  writeForm.classList.add('d-none');
		  setTimeout(function(){
			  alert('로그인 후 이용하세요');
			  location.replace('/login.do');
		  },1000*10);
	  }
	  
	  const usernameModal=document.querySelector('#username-modal');
	  const userBbs=document.querySelector('#user-bbs');
	  const selfImage=document.querySelector('#self_image');
	  const boardCount=document.querySelector('#board-count');
	  const likeCount=document.querySelector('#like-count');
	  
	  const gender=document.querySelector('#gender');
	  const inters=document.querySelector('#inters');
	  const education=document.querySelector('#education');
	  const selfIntro=document.querySelector('#self_intro');
	  const joindate=document.querySelector('#joindate');
	  const udtBtn=document.querySelector('#udtBtn');
	  const encodedFile = document.getElementById('bbs-encoded-file-write');
	  const profileMini=document.getElementById('profile-mini');
	  
	  const bbsContentWrite = document.getElementById('bbs-content-write');
	  const btnUpdate=document.getElementById('btn-update');
	  const btnDelete=document.getElementById('btn-delete');
	  
	  let updateForm;
	  
	  btnUpdate.addEventListener('click',()=>{
		  updateForm.bbsUpdate();
	  });
 	  
 	 btnDelete.addEventListener('click',()=>{
 		if(confirm('삭제하시겠어요?')){
 			updateForm.bbsDelete();
		 }
 	 });
 	let more=false; 
 	document.querySelector('#modal-more').addEventListener('click',()=>{
 		more=true;
 		
 	});
 	  
    $('#viewModal').on('hidden.bs.modal', function () {
        if(more){
        	cardContainers.forEach(el=>{
				el.remove(); 
			 });
        	cursor=0;
        	selectBBS(searchUser);
        }else{
        	more=false;
        	searchUser='';
        }
    });
 
 	 //회원정보 모달 정보 요청
 	 $('#viewModal').on('show.bs.modal', function(e) { // show.bs.modal, shown.bs.modal, hide.bs.modal, hidden.bs.modal
 		 
 		 if(!username.value){
 			 e.preventDefault();
 			 return;
 		 }
 		 const id=e.relatedTarget.dataset.value;
 		 
 		  fetch(`/check-id.do?username=${id}`)
 		  .then(response=>response.json())
 		  .then(data=>setModal(data))
 		  .catch(err=>console.log(err));
 	 });
 	 
 	 //회원정보 모달 데이터 출력
 	 function setModal(data){
 		 console.log(data);
 		 console.log('username='+username.value);
 		 searchUser=data.username;
 		 selfImage.src=data.self_image?data.self_image:'/images/profile-basic-board.svg';
 		 usernameModal.textContent=data.username;
 		 joindate.innerText="가입일:"+data.joinDate.substring(0,11);
 		 gender.src=data.gender=='M'?"/images/gender-female.svg":"/images/gender-male.svg";
 		 udtBtn.href="/edit.do";
 		 if(username.value!=data.username)
 			 udtBtn.classList.add('d-none','disabled');
 		 else udtBtn.classList.remove('d-none','disabled');
 		 
 		 inters.innerHTML='';
 		 data.inters.split(',').forEach(inter=>{
 			 let interStr="";
 			 switch(inter){
 			 	case "POL":
 			 		interStr="정치";
 			 		break;
 			 	case "ECO":
 			 		interStr="경제";
 			 		break;
 			 	case "ENT":
 			 		interStr="연예";
 			 		break;
 			 }
 			 
 			 if(interStr){
 				 const span=document.createElement('span');
 				 span.classList.add('badge','bg-secondary','fs-6','mr-2');
 				 span.textContent=interStr;
 				 span.style.marginRight='5px';
 				 inters.append(span);
 			 }
 		 });
 		 switch(data.education){
 		 	case "EL":
 		 		education.textContent="초등학교";
		 		break;
 		 	case "MI":
 		 		education.textContent="중학교";
 		 		break;
 		 	case "HI":
 		 		education.textContent="고등학교";
 		 		break;
 		 	case "CO":
 		 		education.textContent="대학교";
 		 		break;
 		 }
 		 
 		 
 		 boardCount.textContent=data.bbs.length;
 		 
 		 let count=0;
 		 
 		 data.bbs.forEach(bb=>{
 			count+=bb.likes.length; 
 		 });
 		 
 		 likeCount.textContent=count;
 		 
 		 selfIntro.textContent=!data.self_intro?"자기소개가 없어요":data.self_intro;
 		 userBbs.innerHTML='';
 		 if(data.bbs.length>0){
 			 data.bbs.forEach((bb,idx)=>{
 				if(idx<4){ 
	 				if(bb.img){
	 					const imgDiv=document.createElement('div');
	 					imgDiv.classList.add('g-0','rounded','mb-3','mx-3','d-block');
	 					const img=document.createElement('img');
	 		 			img.classList.add('rounded','img-fluid');
	 		 			img.src=bb.content_type+","+bb.img;
	 		 			img.style.width='100%';
	 		 			imgDiv.append(img);
	 		 			userBbs.append(imgDiv);
	 				}else{
	 					const contentDiv=document.createElement('div');
	 					contentDiv.classList.add('rounded','shadow-sm','bg-light','mb-3','py-3','mx-3','d-block','overflow-hidden');
	 					bb.content.split('\r\n').forEach(ct=>{
	 						if(ct){
		 						const p=document.createElement('p');
		 						p.classList.add('h6','text-muted','fw-bold','fs-6');
		 						p.textContent=ct;
		 						contentDiv.append(p);
	 						}
	 					});
	 					contentDiv.style.height='150px';
	 					userBbs.append(contentDiv);
	 				}
 				}
 			 });
 		 }else{
 			const contentDiv=document.createElement('div');
 			contentDiv.classList.add('rounded','shadow-sm','bg-light','mb-3','py-3','mx-3','d-block','overflow-hidden','align-items-center');
 			const p=document.createElement('p');
			p.classList.add('h6','text-muted','fw-bold','fs-6');
			p.textContent='작성한 게시글이 없어요'
			contentDiv.append(p);
			userBbs.append(contentDiv);
 		 }
 	 }
 	 
 	 
 	 
 	  //게시글 입력 폼 
      // 파일 업로드 input 요소
 	 
      const fileInput = document.getElementById('image-upload');
      // 이미지 표시를 담당할 img 요소
      const imageDisplay = document.getElementById('image-display');
      // 이미지 컨테이너
      const imageContainer = document.getElementById('image-container');
      
      imageDisplay.addEventListener('click',()=>{
	  
	    	 if(imageDisplay.src){
	    		 encodedFile.value='';
    			 imageDisplay.src='';
    			 imageDisplay.classList.add('d-none');
	    	 } 
		       
      });
      
      // 파일 선택 시 이벤트 처리
      fileInput.addEventListener('change', function () {
          displayImage(fileInput,encodedFile,imageDisplay);
      });
      // 드래그 오버 시 이벤트 처리
      imageContainer.addEventListener('dragover', function (e) {
          e.preventDefault();
          imageContainer.classList.add('dragover');
      });
      // 드롭 시 이벤트 처리
      imageContainer.addEventListener('drop', function (e) {
          e.preventDefault();
          imageContainer.classList.remove('dragover');
          const file = e.dataTransfer.files[0];
          if (file) {
    		  fileInput.files = e.dataTransfer.files;
    		  displayImage(fileInput,encodedFile,imageDisplay);
    		  imageContainer.classList.remove('d-none');
          }
      });
      // 드래그 영역에서 나가면 스타일 초기화
      imageContainer.addEventListener('dragleave', function () {
          imageContainer.classList.remove('dragover');
      });
      
 	 
 	 
      
      
      
      
      //게시글 작성 버튼 유효성검사/ 데이터 베이스 입력 요청
      const writeBtn=document.querySelector('#btn-write');
      writeBtn.onclick=()=>{
    	  const form=new FormData(document.querySelector('#bbs-form'));
    	  if(!encodedFile.value&&!bbsContentWrite.value.trim()){
    		  alert('이미지 또는 글을 입력해주세요');
    		  return;
    	  }
    	  
    	  form.append("username",username.value);
    	  form.append("profile",profileMini.src);
    
    	  fetch('/bbs/write.do',{method:'POST',body:form})
    	  .then(response=>response.json())
    	  .then(data=>{
    		  if(data=='empty') alert('이미지 또는 글을 입력해주세요');
    		  else writeCard(data);
    	  })
    	  .catch(err=>console.log(err));
      };
      
      
      bbsContentWrite.addEventListener('keydown',()=>{
    	  bbsContentWrite.style.height='1px';
    	  bbsContentWrite.style.height=(12+bbsContentWrite.scrollHeight)+'px';
      })
      
      //게시글 입력 폼 이미지 버튼 클릭시 파일 선택창 띄우기
      const imageBtn=document.querySelector('#btn-image');
      imageBtn.addEventListener('click',()=>{
    	 fileInput.click();
      });
      
      selectBBS();
      window.scrollTo(0,0);
      
      //게시글 입력 폼 이미지 초기화 함수
      function initWriteForm(){
    	  imageDisplay.classList.add('d-none');
    	  encodedFile.value='';
    	  bbsContentWrite.value='';
      }
      
   // 이미지 표시 함수
      function displayImage(fileInput,encodedFile,imageDisplay) {
          const file = fileInput.files[0];
          if (file) {
        	  if(!file.type.startsWith('image/')){
        		  alert('이미지만 등록 가능');
        		  return;
        	  }
                  // FileReader 객체를 사용하여 파일을 읽음
              const reader = new FileReader();
              reader.readAsDataURL(file);
              reader.onload = function (e) {
            	  // 읽은 데이터를 이미지의 src 속성에 할당하여 표시
	        	  imageDisplay.src=e.target.result;
	              encodedFile.value=e.target.result;
	              imageDisplay.classList.remove('d-none');
              
              };
              
          } else {
        	  encodedFile.value='';
        	  imageDisplay.classList.add('d-none');
          }
      }
      
      let lastContainer;
      const emptyContainer=document.querySelector('#empty-container');
      function selectBBS(str){
    	 
  		const form=new FormData();
  		form.append('cursor',cursor);
  		if(str) {
  			form.append('searchUser',searchUser);
  			more=false;
  		}
  		else if(!str&&searchWord) form.append('searchWord',searchWord);
  		
  		fetch('/bbs/select.do',
  				{method:'POST',
  				body:form})
  		.then(response=>response.json())
  		.then(data=>{
  			console.log(data);
  			if(data.length>0){
  				if(cursor==0){
  					lastContainer=document.querySelector('#card-container');
  					emptyContainer.classList.add('d-none');
  				}else{
  					const containers=document.querySelectorAll('.bbs-container');
  					lastContainer=containers[containers.length-1];
  				}
  					
  				data.forEach((dto,idx)=>{
  					writeCard(dto,idx);
  					if(cursor===0) window.scrollTo(0,0)
  					if(idx===0) {
  						cursor=dto.no;
  					}
  					
				});
  			}else{
  				if(cursor==0){
  					emptyContainer.classList.remove('d-none');
  				}else{
  					emptyContainer.classList.add('d-none');
  				}
  			}
  		})
  		.catch(err=>console.log(err));
  	}
      
      const scrollObserver=new IntersectionObserver(onScroll,{
    	  root:null,
    	  margin:'0px',
    	  threshold:0.3
      });
      
     
      
      const observer=new IntersectionObserver(onIntersect,{
    	  root:null,
    	  margin:'0px',
    	  threshold:0.3
      });
      
      function onScroll(entries,observer){
    	  entries.forEach(entry=>{
    		  if(entry.isIntersecting){
    			 if(searchUser)
    				 selectBBS(searchUser);
    			 else
    				 selectBBS();
	    		 observer.unobserve(entry.target); 
    		  }
    	  });
      }
      
      
      
      
      function onIntersect(entries,observer){
    	  entries.forEach(entry => {
    		  if(entry.isIntersecting){
    	        const img = entry.target;
    	        img.src = img.dataset.value;
    	        img.dataset.value = '';
    	        observer.unobserve(img);
    		  }
    	   });
      }
      
      
      
      //json 데이터 카드로 변환 함수
      function writeCard(data,idx){
    	  if(username.value)initWriteForm();
    	  
          const bbsContainer=document.querySelector('#bbs-container');
          const newContainer=bbsContainer.cloneNode(true);
          const bbsProfile=newContainer.querySelector('#bbs-profile');
          const bbsUsername=newContainer.querySelector('#bbs-username');
          const bbsPostDate=newContainer.querySelector('#bbs-postdate');
          const moreBtn=newContainer.querySelector('#bbs-btn-more');
          const bbsImgContainer=newContainer.querySelector('#bbs-img-container');
          const bbsContentContainer=newContainer.querySelector('#bbs-content-container');
          const bbsContentOne=newContainer.querySelector('#bbs-content-one'); //내용 한줄 미리보기 (이미지 있을 경우)
          const bbsContentAll=newContainer.querySelector('#bbs-content-all'); //내용 한줄 제외 나머지 (이미지 있을 경우)
          const bbsContentMore=newContainer.querySelector('#bbs-content-more'); //한줄 이상일 경우 더보기 버튼 (?줄 이상)
          const bbsNo=newContainer.querySelector('#bbs-content-more');
          const bbsBtnImage=newContainer.querySelector('#bbs-btn-image');
          const bbsBtnWrite=newContainer.querySelector('#bbs-btn-write');
          const bbsEncodedFile=newContainer.querySelector('#bbs-encoded-file');
          const bbsSavedFile=newContainer.querySelector('#bbs-saved-file');
          const bbsInputFile=newContainer.querySelector('#bbs-input-file');
          const bbsContentUpdate=newContainer.querySelector('#bbs-content-update');
          const bbsLikeContainer=newContainer.querySelector('#bbs-like-container');
 		  const bbsLikeBtn=newContainer.querySelector('#bbs-like-btn');
 		  const bbsLike=newContainer.querySelector('#bbs-like');
          const bbsFooterContainer=newContainer.querySelector('#bbs-footer-container');
 		  const bbsCollapseOne=newContainer.querySelector('#bbs-collapseOne');
 		  
          let bbsImg; 
          
          if(data.likes.length){
        	  bbsLike.textContent=data.likes.length;
        	  if(data.likes.some(like=>like.username==username.value))
            	  bbsLike.previousElementSibling.src=window.location.origin+'/images/like.svg';
              else bbsLike.previousElementSibling.src=window.location.origin+'/images/like-line.svg';
          }
          
          bbsLikeBtn.addEventListener('click',(e)=>{
        	  if(!username.value) return;
        	  const img=e.target;
        	  let url;
        	  if(img.src==window.location.origin+'/images/like-line.svg'){
        		  e.target.src=window.location.origin+'/images/like.svg';
        		  e.target.nextElementSibling.textContent=parseInt(e.target.nextElementSibling.textContent)+1;
        		  url='/bbs/likes/insert'
        	  }
        	  else{
        		  e.target.src=window.location.origin+'/images/like-line.svg';
        		  e.target.nextElementSibling.textContent=parseInt(e.target.nextElementSibling.textContent)-1;
        		  url='/bbs/likes/delete'
        	  }
        	  
        	  const likes={
        		username:username.value,
        		no:bbsNo.value
        	  }
        	  fetch(url,{method:'POST',headers:{"Content-Type":"application/json"},body:JSON.stringify(likes)})
        	  .then(response=>response.text())
        	  .then(data=>{
        		  console.log(data);
        	  })
        	  .catch(err=>console.err(err));
        	  
        	  
        	  //children[0].src='/images/like.svg';
        	  //children[1].textContent=parseInt(children[1].textContent)+1;
        	  
          });          
          //수정 버튼 클릭시 로직
          bbsBtnWrite.addEventListener('click',()=>{
        	  if(!bbsSavedFile.value&&!bbsContentUpdate.value.trim()){
        		  alert('이미지 또는 글을 입력해주세요');
        		  return;
        	  }
        	  
        	  const form=new FormData();
        	  form.append("no",bbsNo.value);
        	  form.append("username",bbsUsername.textContent);
        	  form.append("content",bbsContentUpdate.value);
        	  if(bbsEncodedFile.value!=bbsSavedFile.value){
        		  form.append("encoded-file",bbsEncodedFile.value);
        	  }
        	  if(bbsSavedFile.value)
        		  form.append("savedFile",bbsSavedFile.value);
        	  fetch('/bbs/edit.do',{method:'POST',body:form})
        	  .then(response=>response.json())
        	  .then(data=>{
        		  if(data){
        			  if(data.error) {
        				  alert('이미지 또는 글을 작성해주세요');
        				  return;
        			  }
        			  bbsContentOne.innerHTML='';
        			  bbsContentAll.innerHTML='';
        			  setBBSContent(data.content,
        					  bbsContentContainer,
        					  bbsContentOne,
        					  bbsContentAll,
        					  bbsContentMore);
        		  
        		  bbsContentUpdate.classList.add('d-none');
    			  bbsBtnImage.classList.add('d-none');
    			  bbsBtnWrite.classList.add('d-none');
    			  bbsLikeContainer.classList.remove('d-none');
    			  bbsFooterContainer.classList.remove('d-none');
    			  moreBtn.classList.remove('d-none');
        		  }
			  })
        	  .catch(err=>console.log(err));
          });
          
          bbsBtnImage.addEventListener('click',()=>{
        	 bbsInputFile.click();
          });
          
          bbsInputFile.addEventListener('change', function () {
        	  if(!bbsImg){
        		  bbsImg=document.createElement('img');
    		      bbsImg.classList.add('card-img-top','img-fluid','card-body-img');
    		      bbsImgContainer.append(bbsImg);
        	  }
        	  bbsImgContainer.classList.remove('d-none');
              displayImage(bbsInputFile,bbsEncodedFile,bbsImg);
          });
          
          bbsNo.value=data.no;
          if(data.no==1) cursor=-1;
          bbsProfile.src=data.profile?data.profile:"/images/profile-basic-board.svg";
          bbsProfile.classList.add('rounded-circle');
          bbsProfile.parentElement.dataset.value=data.username;
          bbsUsername.textContent=data.username;
         
          bbsPostDate.textContent=getTimeDifference(data.postdate);
          if(data.img){
		      bbsImg=document.createElement('img');
		      bbsImg.classList.add('card-img-top','img-fluid','card-body-img');
		      if(idx<4){
		    	  bbsImg.src=data.img;  
		      }else{
		    	  bbsImg.dataset.value=data.img;  
		    	  observer.observe(bbsImg);
		      }
		      bbsImg.addEventListener('click',()=>{
		    	 if(bbsImg.src&&moreBtn.classList.contains('d-none')){
		    		 bbsSavedFile.value='';
		    		 bbsEncodedFile.value='';
	    			 bbsImg.src='';
	    			 bbsImgContainer.classList.add('d-none');
		    	 } 
		      });
		      bbsImgContainer.append(bbsImg);
		      bbsSavedFile.value=data.img;
          }
          
          if(username.value!=data.username) moreBtn.remove();
          else{
        	  moreBtn.addEventListener('click',()=>{
        		  if(updateForm)updateForm.writeBtnClick();
        		  updateForm=newContainer;
        	  });
          }
          bbsContentUpdate.value=data.content?data.content:'';
          bbsContentUpdate.style.height=newContainer.querySelector('.card-body').height;
          bbsContentUpdate.addEventListener('keydown',()=>{
        	  bbsContentUpdate.style.height='1px';
        	  bbsContentUpdate.style.height=(12+bbsContentUpdate.scrollHeight)+'px';
          })
          
          
          setBBSContent(data.content,
        		  bbsContentContainer,
        		  bbsContentOne,
        		  bbsContentAll,
        		  bbsContentMore);
          
          function writeBtnClick(){
        	  bbsBtnWrite.click();
          }
          
          function bbsUpdate(){
        	 bbsContentUpdate.classList.remove('d-none');
     		 bbsBtnImage.classList.remove('d-none');
     		 bbsBtnWrite.classList.remove('d-none');
    		 bbsContentContainer.classList.add('d-none');
    		 bbsContentOne.classList.add('d-none');
     		 moreBtn.classList.add('d-none');
     		 bbsLikeContainer.classList.add('d-none');
     		 bbsFooterContainer.classList.add('d-none');
     		 newContainer.focus();
     		  $('#bottomModal').on('hidden.bs.modal', function () {
     		        // 모달이 닫힐 때 특정 요소에 포커스를 설정
     			 bbsContentUpdate.focus();
     		  });
          }
          
          function bbsDelete(){
        	  const form=new FormData();
        	  form.append('no',bbsNo.value);
        	  form.append('username',bbsUsername.textContent);
        	  fetch('/bbs/delete.do',{method:'POST',body:form})
 			 .then(response=>response.json())
 			 .then(data=>{
 				 if(data){
 					 newContainer.style.animation='move 1s';
 					 setTimeout(()=>{
 						newContainer.remove();
 					 },900)
 					cardContainers=cardContainers.filter(x=>x.id!=`bbs-container${data.no}`);
 				 }
 			 })
 			 .catch(err=>console.log(err));
          }
          
          if(idx==7){
        	  scrollObserver.observe(newContainer);
          }
          newContainer.addEventListener('blur',()=>{
        	 if(!bbsContentUpdate.classList.contains('d-none')) bbsUpdate(); 
          });
          newContainer.bbsDelete=bbsDelete;
          newContainer.bbsUpdate=bbsUpdate;
          newContainer.writeBtnClick=writeBtnClick;
          newContainer.querySelectorAll('*').forEach(element=>{
        	  if(element.id)element.id=element.id+data.no;
          });
          bbsContentMore.firstElementChild.firstElementChild.dataset.target=`#${bbsCollapseOne.id}`;
          bbsContentMore.firstElementChild.firstElementChild.setAttribute('aria-controls', `#${bbsCollapseOne.id}`);
          bbsCollapseOne.dataset.parent=`#${bbsContentContainer.id}`;
  
          
          newContainer.classList.remove('d-none')
          newContainer.style.animation='move_ 1s, appear 1s';
          setTimeout(()=>{
        	  newContainer.style.zIndex='auto';
          },1000)
          
          
          if(typeof idx=="undefined"){
        	  document.querySelector('#card-container').after(newContainer);
        	  cardContainers.push(newContainer);
          }else{
        	  cardContainers.unshift(newContainer);
        	  lastContainer.after(newContainer);
          }
      }
      
      //게시글 내용 출력 함수
      function setBBSContent(content,
    		  bbsContentContainer,
    		  bbsContentOne,
    		  bbsContentAll,
    		  bbsContentMore){
    	  if(content) {
    		  content=content.split('\r\n');
    		  if(!content[content.length-1].trim()) content.pop();
    	  }else content='';
    	  
    	  
    	  if(content.length>1){
        	  const p=document.createElement('p');
        	  p.classList.add('lead','text');
        	  p.textContent=content[0];
        	  bbsContentOne.append(p);
        	  bbsContentOne.classList.remove('d-none');
        	  for(let i=1; i<content.length; i++){
        		  const p2=document.createElement('p');
            	  p2.classList.add('lead','text');
            	  if(content[i]=='')
            		  p2.innerHTML='<br/>';
            	  else
            		  p2.textContent=content[i];
            	  bbsContentAll.append(p2);
        	  }
        	  const bbsContentMoreBtn=bbsContentMore.firstElementChild.firstElementChild;
        	  const bbsCollapse=bbsContentContainer.firstElementChild.firstElementChild;
        	  
        	  if(bbsCollapse.classList.contains('show')){
        		  bbsContentMore.classList.add('d-none');
        	  }
        	  
        	  if(bbsContentMoreBtn.getAttribute('aria-expanded')=='false'){
        		  bbsContentMoreBtn.setAttribute('aria-expanded','true');
        	  }
        	  bbsContentContainer.classList.remove('d-none');
        	  bbsContentMore.addEventListener('click',()=>{
        		  bbsContentMore.classList.add('d-none');
        	  });
          }else{
        	  p=document.createElement('p');
        	  p.classList.add('lead','text');
        	  p.textContent=content[0];
        	  bbsContentOne.append(p);
        	  bbsContentOne.classList.remove('d-none');
        	  bbsContentContainer.classList.add('d-none');
          }
      }
      
      
     //게시글 작성 시간 반환
      function getTimeDifference(time){
    	  const currentTime=new Date();
    	  const compareTime=new Date(time);
    	  const difference=currentTime-compareTime;
    	  
    	  const minutesDifference = Math.floor(difference / (1000 * 60));
    	  const hoursDifference = Math.floor(difference / (1000 * 60 * 60));
    	    
    	if(minutesDifference <= 1) {
	        return "방금 전";
	    }else if (minutesDifference < 60) {
	        return `${minutesDifference}분 전`;
	    }else if (hoursDifference < 24) {
	        return `${hoursDifference}시간 전`;
	    }else {
	        return time.substring(0,11);
	    }  
    	    
      }
      
  
    
          
});