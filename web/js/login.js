/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function login(){

    let _url= 'api/login/login';
    let _username=document.getElementById('j_username').value;
    let _password=document.getElementById('j_password').value;
    let user={
        tempUser:_username,
        password:_password,
        department:null
    };
    $.ajax({
        type: "POST",
        url: _url ,
        contentType: "application/json",
        data: JSON.stringify(user),
        success: function (user_role) {
            sessionStorage.USER= user_role.user.tempUser;
            sessionStorage.ROLE=user_role.role;
            if(sessionStorage.ROLE==='Secretaria de Alcaldia'){
                  mainSW().then(()=>{
                      window.location.replace("/Kronos/index"); 
                  });
                
               
  
            }
          
            else{
                  if (document.referrer === 'http://localhost:8080/Kronos/addAccord.jsp' && user_role.role === 'Concejo Municipal')
                    window.location.replace("/Kronos/addAccord");
                else
                    window.location.replace("/Kronos/index"); 
            }
            

         
         
      },
        error: function (response) {
            document.getElementById('alertDiv').style.display='block';
        }
    });
    
   
}

function logout(){
   delete sessionStorage.USER;
   delete sessionStorage.ROLE;
   window.location.replace("/Kronos/index"); 
}
