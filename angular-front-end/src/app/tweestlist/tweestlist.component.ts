import { Component, OnInit } from '@angular/core';
import {HttpClient,HttpParams} from '@angular/common/http'
import {ActivatedRoute} from '@angular/router';

import {ApiRequestService} from '../api-request.service';

@Component({
  selector: 'app-tweestlist',
  templateUrl: './tweestlist.component.html',
  styleUrls: ['./tweestlist.component.css']
})
export class TweestlistComponent implements OnInit {
  tweets:any={};
  id:number;
  like:boolean;
  //baseUrl:string='http://restapi-env.eba-xd2trzjb.us-east-2.elasticbeanstalk.com/';

  constructor(
    private http: HttpClient,
    private route:ActivatedRoute,
    private apiRquest:ApiRequestService
  ) { }

  ngOnInit(): void {
    this.getId(); 
    this.like=false;
  }

  getId(){
    console.log(this.route.toString());
    this.id= +this.route.parent.snapshot.paramMap.get("id");
    console.log(this.id);
    this.tweets=this.gettweets();
    console.log(this.tweets);
  }

  gettweets(){
    this.apiRquest.get("useralltweets/"+this.id,new HttpParams())
    .subscribe(res=>{
        this.tweets=res.body;
    })
  }

  likeclick(i:number){
    let count=this.tweets[i]["likes"];
      if(count<0){
        count=-count;
        this.tweets[i]["likes"]=count-1;
      }
      else{
        count=count+1;
        this.tweets[i]["likes"]=-count;
      }
  }

  commentclick(){

  }

}
