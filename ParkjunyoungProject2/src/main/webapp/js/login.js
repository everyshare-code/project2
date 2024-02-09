/**
 * 
 */


document.addEventListener('DOMContentLoaded',()=>{
	
	
	
	const username=document.querySelector('#username');
	const password=document.querySelector('#password');
	const passwordH=document.querySelector('#password-h');
	const savedUsername=localStorage.getItem("username")?
			atob(localStorage.getItem("username")):"";
				
	const loginStatus=document.querySelector('#login-status');
	if(loginStatus==1) location.replace('/index.do')
			
	if(savedUsername) username.value=savedUsername;
	
	if(!username.value) username.focus();
	
	passwordH.addEventListener('focus',()=>{
		if(!passwordH.value){
			passwordH.setAttribute("hidden","hidden");
			password.removeAttribute("hidden");
			password.focus();
		}
	});
	
	


	password.addEventListener('blur',()=>{
		if(!password.value){
			password.setAttribute("hidden","hidden");
			passwordH.removeAttribute("hidden");
		}
	});
	
	
	
});





