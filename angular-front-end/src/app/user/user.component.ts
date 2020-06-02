import { Component, OnInit } from '@angular/core';
import {HttpClient,HttpParams} from '@angular/common/http'
import {ActivatedRoute} from '@angular/router';

import {ApiRequestService} from '../api-request.service';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  tweets:any={};
  id:number;
  baseUrl:string='http://10.20.20.76:8080/';

  constructor(
    private http: HttpClient,
    private route:ActivatedRoute,
    private apiRquest:ApiRequestService
  ) { }

  ngOnInit(): void {
    this.getId(); 
  }


  getId(){
    this.id= +this.route.snapshot.paramMap.get("id");
    console.log(this.id);
    this.tweets=this.gettweets();
    console.log(this.tweets);
  }

  gettweets(){
    this.apiRquest.get(this.baseUrl+"usertweets/"+this.id,new HttpParams())
    .subscribe(res=>{
        this.tweets=res.body;
    })
  }
}
