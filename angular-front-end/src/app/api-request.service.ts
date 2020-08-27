import { Injectable } from '@angular/core';
import {HttpClient, HttpParams,HttpErrorResponse, HttpHeaders} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

import {TokenstoreserviceService} from './tokenstoreservice.service';


@Injectable({
  providedIn: 'root'
})
export class ApiRequestService {

  baseUrl:string='http://10.20.20.38:8080/';


  constructor(
    private http:HttpClient,
    private tokenstore: TokenstoreserviceService
  ) { }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };

  private getHeader():HttpHeaders{
    let header=new HttpHeaders();
    let token='Bearer '+this.tokenstore.getToken();
    // header = header.append('Content-Type', 'application/json');
    if(token!==null){
      header=header.append("Authorization", token);
      console.log("in"+token);
    }
    return header;
  }

  get(url:string,params:HttpParams){
    return this.http.get(this.baseUrl+url,{params:params,headers:this.getHeader(), observe: "response"})
              .pipe(
                
                catchError(this.handleError)
              );
  }

  post(url:string, data:any){
    return this.http.post(this.baseUrl+url,data,{headers:this.getHeader(),observe:"response"})
                .pipe(
                  catchError(this.handleError)
                );
  }
}
