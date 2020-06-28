import { Component, OnInit } from '@angular/core';

import {HttpClient,HttpParams} from '@angular/common/http'
import {ActivatedRoute} from '@angular/router';

import {ApiRequestService} from '../api-request.service';


@Component({
  selector: 'app-my-tweets',
  templateUrl: './my-tweets.component.html',
  styleUrls: ['./my-tweets.component.css']
})
export class MyTweetsComponent implements OnInit {
  tweets:any={};
  id:number;
  //baseUrl:string='http://10.20.20.76:8080/';
  //baseUrl:string='http://restapi-env.eba-xd2trzjb.us-east-2.elasticbeanstalk.com/';

  constructor(
    private http: HttpClient,
    private route:ActivatedRoute,
    private apiRquest:ApiRequestService
  ) { }

  ngOnInit(): void {
    this.getId(); 
  }

  getId(){
    console.log(this.route.toString());
    this.id= +this.route.parent.snapshot.paramMap.get("id");
    console.log(this.id);
    this.tweets=this.gettweets();
    console.log(this.tweets);
  }

  gettweets(){
    this.apiRquest.get("usertweets/"+this.id,new HttpParams())
    .subscribe(res=>{
        this.tweets=res.body;
    })
  }


}
