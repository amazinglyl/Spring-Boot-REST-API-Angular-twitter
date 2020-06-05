import { Component, OnInit } from '@angular/core';
import {LoginServiceService} from '../login-service.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model:any={};

  constructor(
    private loginService:LoginServiceService,
    private router:Router
  ) { }

  ngOnInit(): void {
  }

  login(){
      this.loginService.get(this.model.name,this.model.password)
          .subscribe(res=>{
              if(res.status===200){
                let id:number=res.body["id"];
                console.log(id);
                this.router.navigate(['/user',id,'home'])
              }
          })
  }

}
