import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenstoreserviceService {

  public key: string ='userToken';
  public storage: Storage=sessionStorage;
  constructor() { }

  store(jwtToken:string){
    this.storage.setItem(this.key,jwtToken);
  }

  getToken(){
    try{
      let token:string = this.storage.getItem(this.key);
      if (token) {
          return token;
      }
      else{
          return null;
      }
  }
  catch (e) {
      return null;
  }
  }
}
