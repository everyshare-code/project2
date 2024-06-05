/**
 * 
 */

window.addEventListener('DOMContentLoaded',function() {
	const username=document.querySelector('#username');
	const usernameMsg=document.querySelector('#usernameMsg');
	const password=document.querySelector('#password');
	const passwordMsg=document.querySelector('#passwordMsg');
	const passwordCheck=document.querySelector('#passwordCheck');
	const passwordCheckMsg=document.querySelector('#passwordCheckMsg');
	const intersMsg=document.querySelector('#intersMsg');
	const educationMsg=document.querySelector('#educationMsg');
	const passwordPattern=/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/gi;
	const usernamePattern=/^(?=.*[A-Za-z])[A-Za-z\d]{2,}$/g;
	const selfImage=document.querySelector('#self-image');
	const fileInput=document.querySelector('#file-input');
	const fileWarning=document.querySelector('#file-warning');
	const selfImageDiv=document.querySelector('#self-image-div');
	const encodedFile=document.querySelector('#encoded-file');
	const intersContainer=document.querySelector('.inters-container');
	const closeBtn=document.querySelector('.btn-close');
	const defaultImageSrc=selfImage.src;
	const form=document.forms[0];
	let isDuplicate=false;
	const notValidMsg={
		"usernameMsg":["아이디를 입력해주세요","아이디는 10자 이하 영문,숫자 조합","이미 사용중인 아이디"],
		"passwordMsg":["비밀번호를 입력해주세요","비밀번호는 영문,숫자,특수문자를 포함한 8자 이상"],
		"passwordCheckMsg":["비밀번호 확인을 입력해주세요","비밀번호와 불일치"],
		"intersMsg":["관심사항은 하나 이상 선택"],
		"educationMsg":["학력을 선택해주세요"],
		"file-warning":["이미지 파일만 선택 가능.","파일 크기는 5MB 이하로 선택","프로필 이미지를 등록하려면 이미지를 클릭"]
	};
	const validMsg={
		"usernameMsg":"사용 가능한 아이디",
		"passwordMsg":"사용 가능한 비밀번호",
		"passwordCheckMsg":"비밀번호와 일치"
	}
	
	closeBtn.classList.add('d-none');
	
	
	closeBtn.addEventListener('click',()=>{
		closeBtn.classList.add('d-none');
		encodedFile.value="";
		selfImage.src=defaultImageSrc;
		fileWarning.textContent=getMsg(fileWarning,2);
		fileWarning.style.color="#6c757d";
	});
	
	form.onsubmit=(e)=>{
	    const inters=document.querySelectorAll('input[type=checkbox]:checked');
	    const education=document.querySelector('#education');
	    let notValidElement=null;
		let idx=0;
		
	    if(username.value.trim()==""){ //아이디 빈값
	        notValidElement=usernameMsg;
	    }else if(password.value.trim()==""){ //비밀번호 빈값
	        notValidElement=passwordMsg;
	    }else if(passwordCheck.value.trim()==""){ //비밀번호 확인 빈값
	        notValidElement=passwordCheckMsg;
	    }else if(inters.length==0){ //관심사항 미선택
	        notValidElement=intersMsg;
	    }else if(education.selectedIndex==0){ //학력사항 미선택
			notValidElement=educationMsg;
	    }else if(!password.value.match(passwordPattern)){ //비밀번호 패턴 불일치
			notValidElement=passwordMsg;
			idx=1;
	    }else if(password.value!==passwordCheck.value){ //비밀번호, 비밀번호 확인 불일치
	        notValidElement=passwordCheckMsg;
			idx=1;
	    }else if(!username.match(usernamePattern)){ //아이디 패턴 불일치
			notValidElement=usernameMsg;
			idx=1;
		}else if(isDuplicate){
			notValidElement=usernameMsg;
			idx=2;
		}
	    
		failedText(notValidElement,idx,e.target);
		e.preventDefault();
		
        
		
		return;
	
	};
	
	/*
	function encodeBase64(img){
		const canvas = document.createElement("canvas");
        canvas.height = img.height;
        canvas.width = img.width;
        const ctx = canvas.getContext("2d");
        ctx.drawImage(img,0,0,img.width,img.height);
        return canvas.toDataURL();
	}*/

	intersContainer.addEventListener('change',()=>{
		if(document.querySelectorAll('input[name=inters]:checked').length>0) successText(intersMsg);
		else failedText(intersMsg); 
	});

	
	password.onblur=()=>{
	    if(password.value.match(passwordPattern)){
	        successText(passwordMsg);
	    }else{
	        failedText(passwordMsg,1);
	    }
	};
	
	passwordCheck.onkeyup=()=>{
	    if(password.value && password.value==passwordCheck.value){
	        successText(passwordCheckMsg);
	    }else if(password.value!==passwordCheck.value){
	        failedText(passwordCheckMsg,1);
	    }
	};
	
	
	username.onblur=()=>{
		let id=username.value;
		if(username.value.length>0 && username.value.match(usernamePattern)){
	        const url=document.querySelector('#checkURL').value+"?username="+id;
	        fetch(url)
	        .then(response=>response.json())
	        .then(data=>{
	        	console.log(data);
				if(data){
					isDuplicate=true;
					failedText(usernameMsg,2);
				}
				else{
					isDuplicate=false;
					successText(usernameMsg);
				}
			})
			.catch(err=>console.log(err));
	    }else{
	        failedText(usernameMsg,1);
	    }
	 };
	 
	
	
	
	selfImageDiv.addEventListener('click',(e)=>{
		if(e.target.nodeName=='BUTTON') {
			closeBtn.click();
			e.preventDefault();
			return;
		}
		fileInput.click();
	});
	
	fileInput.addEventListener('change',handleFileSelect);
	
	function handleFileSelect() {
	    // 선택된 파일에 대한 로직을 추가할 수 있습니다.
	    const selectedFile = fileInput.files[0];
	    // 이미지 파일만 허용 (확장자로 체크)
	    if (selectedFile && selectedFile.type.startsWith('image/')) {
	    	if(selectedFile.size>(1024*1024*5)) { //파일크기 5MB 초과시 
	    		closeBtn.click();
	    		failedText(fileWarning, 1);
	    		return;
	    	}
	        // 이미지 파일인 경우 미리보기로 보여줌
	        const reader = new FileReader();
	        reader.readAsDataURL(selectedFile);
	        reader.onload = function (e) {
	            selfImage.src = e.target.result;
	            selfImage.style.width = selfImage.clientWidth + 'px';
	            selfImage.style.height = selfImage.clientHeight + 'px';
	            encodedFile.value=e.target.result;
	            closeBtn.classList.remove("d-none");
	            // 경고 메시지 초기화
	            successText(fileWarning);
	        };
	        
	    } else if (selectedFile && !selectedFile.type.startsWith('image/')) {
	        // 이미지 파일이 아닌 경우 경고 메시지 표시
	    	closeBtn.click();
	        failedText(fileWarning, 0);
	    } 
	}
	
	
	
	function getMsg(element,idx){
		return notValidMsg[element.id][idx];
	}

	function failedText(element,idx,target){
		let elementId=element.id;
		if(elementId!="intersMsg") elementId=elementId.replace("Msg","");
		element.textContent=getMsg(element,idx);
		element.style.color="red";
	    element.style.animation="blink-text 0.4s step-end infinite"
		setTimeout(function(){element.style.animation=""},2000);
		if(target) target.focus();
	}
	
	function successText(element){
		element.textContent=element.id==null?"":validMsg[element.id];
		element.style.animation="";
		element.style.color="#64A9FA"
	}
 
});
 