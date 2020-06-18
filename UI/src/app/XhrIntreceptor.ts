import { HttpInterceptor } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class XhrIntreceptor implements HttpInterceptor{

constructor(private cookieService: CookieService){

}


  intercept(req: import("@angular/common/http").HttpRequest<any>, next: import("@angular/common/http").HttpHandler): import("rxjs").Observable<import("@angular/common/http").HttpEvent<any>> {
    var xhr= req.clone({
      headers:req.headers.set('X-Requested-With', 'XMLHttpRequest')
                          // .set("X-Auth",localStorage.getItem('token'))
                          // .set('X-XSRF-TOKEN',this.cookieService.get('XSRF-TOKEN')),
      //disable this in prod
      // withCredentials:true
    });
  
  if(this.cookieService.get('XSRF-TOKEN')){
    console.log(this.cookieService.getAll());

    xhr = xhr.clone({
      headers:xhr.headers.set('X-XSRF-TOKEN',this.cookieService.get('XSRF-TOKEN'))

    }
  );
  }

    return next.handle(xhr);    
  }
  
}