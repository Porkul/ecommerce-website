import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { OKTA_AUTH } from '@okta/okta-angular';
import { OktaAuth } from '@okta/okta-auth-js';
import { from, lastValueFrom, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})

// for securing FrontEnd
export class AuthInterceptorService implements HttpInterceptor {

  constructor(@Inject(OKTA_AUTH) private oktaAuth: OktaAuth) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return from(this.handleAccess(request, next));
  }

  private async handleAccess(request: HttpRequest<any>, next: HttpHandler): Promise<HttpEvent<any>> {

    // Only add an access token for secured endpoints
    const theEndpoint = environment.luv2shopApiUrl + '/orders';
    const securedEndpoints = [theEndpoint];

    // check if current url is secured
    if (securedEndpoints.some(url => request.urlWithParams.includes(url))) {

      // get access token from oktaAuth
      const accessToken = this.oktaAuth.getAccessToken();

      // clone the request and add new header with access token
      // And then remember the reason we have to clone
      // is because the request is immutable!
      // meaning that you can't change it directly.
      // So you have to make a copy of it, like cloning it,
      // and then setting the headers accordingly.
      request = request.clone({
        setHeaders: {
          Authorization: 'Bearer ' + accessToken
        }
      });
    }

    //  return, next.handle(request) to promise. 
    // saying, "Go ahead and continue the other interceptors that are in the chain,"
    // if there are NO other interceptors in order to simply make the call to the given REST API.
    return await lastValueFrom(next.handle(request));
  }
}
