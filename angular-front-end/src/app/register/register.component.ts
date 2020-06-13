import { Component, OnInit } from '@angular/core';
import {HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';

import {ApiRequestService} from '../api-request.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  baseUrl:string='http://10.20.20.76:8080/';

  userData:any={};

  constructor(
    private apiRquest:ApiRequestService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  submit(){
    this.apiRquest.post(this.baseUrl+"users",this.userData)
                  .subscribe(
                    res=>{
                      if(res.status==200)
                      this.router.navigate(['/login'])
                    }
                  )
  }
}
