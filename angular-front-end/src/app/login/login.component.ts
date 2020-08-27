import { Component, OnInit } from '@angular/core';
import {LoginServiceService} from '../login-service.service';
import {Router} from '@angular/router';
import {TokenstoreserviceService} from '../tokenstoreservice.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model:any={};
  fail:string;

  constructor(
    private loginService:LoginServiceService,
    private router:Router,
    public tokenstore: TokenstoreserviceService
  ) { }

  ngOnInit(): void {
  }

  login(){
      this.loginService.post(this.model.name,this.model.password)
          .subscribe(res=>{
              console.log(res.status);
              let id:number=res.body["id"];
              this.tokenstore.store(res.body["token"]);
              console.log(res.body["token"]);
              console.log(id);
              this.router.navigate(['/user',id,'home'])
          },
          err=>{
            this.fail="failed login"
          }
          )
  }

}
