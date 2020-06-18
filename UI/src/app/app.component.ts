import { Component } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AppService } from './app.service';
import { Router } from '@angular/router';
import { finalize, retry, catchError } from 'rxjs/operators';
import { Config } from 'protractor';
import { throwError } from 'rxjs';
// import * as $ from 'jquery';
import * as bootstrap from "bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'my-app';

  constructor(private http:HttpClient, private appService:AppService,private router: Router){
    //code for cleaning up when model is closed
    $('#loginModal').on('hide.bs.modal', function (e) {
      console.log('Modal got hidden');
      document.getElementById("otp-form-group").style.display='none';
      document.getElementById("sendOTP-button").style.display='block';
      document.getElementById("verifyOTP-button").style.display='none';  
      document.getElementById('error-message').innerHTML='';
    });
  }

  otpLoginDetails={mobileNumber:'',otp:''};

  logout(){
    this.http.post("/logout",{observe:'response'}).subscribe((resp)=>{
        console.log(resp);
        console.log("inside finally logout()");
        this.showSnackbarMessage("Successfully logged out");
      });
  }

    login(){
      console.log("inside login");
      this.http.get("/isAuthenticated")
      .pipe(retry(3), catchError(this.handleAuthenticationError))
      .subscribe(()=>{},(resp)=>{
        console.log(resp);
      });

      // $('#loginModal').modal('show');

    }

    sendOTP(){
      console.log("sending OTP");
      this.http.get("/getOTP/"+this.otpLoginDetails.mobileNumber,{observe:'response'}).subscribe((resp)=>{
        if(resp.status==200){
          console.log("Succesfully sent OTP");
          document.getElementById("otp-form-group").style.display='block';
          document.getElementById("sendOTP-button").style.display='none';
          document.getElementById("verifyOTP-button").style.display='block';  
        } 
      });

    }

    verifyOTP(){
      console.log("Verifying OTP");
      console.log(this.otpLoginDetails.mobileNumber+" "+this.otpLoginDetails.otp);
      let formData = new FormData();
      formData.append('user','+'+this.otpLoginDetails.mobileNumber);
      formData.append('otp',this.otpLoginDetails.otp);

      this.http.post("/otplogin",formData,{observe:"response"}).subscribe((resp)=>{
        console.log(resp);
        if(resp.status<300 && resp.status>=200){
          console.log("Succesfully logged in");
          this.closeModal();
          this.showSnackbarMessage("Successfully logged in");
        }
        else{
          console.log("bad credentials");
          this.setModalErrorMessage("Could not authenticate the OTP");
        }
      },(resp)=>{
        if(resp.status==401){
          console.log("bad credentials");
          this.setModalErrorMessage("Could not authenticate the OTP");
        }
      });

    }

    showModal(){
      $('#loginModal').modal('show');
    }

    closeModal(){
      document.getElementById("otp-form-group").style.display='none';
      document.getElementById("sendOTP-button").style.display='block';
      document.getElementById("verifyOTP-button").style.display='none';  
      document.getElementById('error-message').innerHTML='';
      $('#loginModal').modal('hide');
    }

    setModalErrorMessage(message){
      document.getElementById('error-message').innerHTML=message;
    }

    handleAuthenticationError(error: HttpErrorResponse){
      if (error.error instanceof ErrorEvent) {
        // A client-side or network error occurred. Handle it accordingly.
        this.handleClientSideErrors(error);
      } else {
        // The backend returned an unsuccessful response code.
        // The response body may contain clues as to what went wrong,
        
        if(error.status == 401){
          console.log("opening modal");
          
           $('#loginModal').modal('show');
        }
        return throwError(
          'Not Authenticated.');
      }
      // return an observable with a user-facing error message
    return throwError(
    'Something bad happened; please try again later.');
    }

    handleClientSideErrors(error: HttpErrorResponse){
      console.error('An error occurred:', error.error.message);
    }

    hitSecureAPI(){
      
      document.getElementById('response-message').innerHTML='';

      this.login();

      this.http.get(environment.apiEndPoint+'/message').subscribe((data)=>{
        console.log(data);
        document.getElementById('response-message').innerHTML=data["message"];
      });
    }
  
    hitUnSecureAPI(){
      
      document.getElementById('response-message').innerHTML='';
      this.http.get('/unprotected',{observe:'body'}).subscribe((data)=>{
        console.log(data);
        document.getElementById('response-message').innerHTML=data["message"];
      });
    }
    showSnackbarMessage(message) {
      var x = document.getElementById("snackbar");
      x.innerHTML=message;
      x.className = "show";
      setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
    }
}