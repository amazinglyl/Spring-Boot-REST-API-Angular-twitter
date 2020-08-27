import { Component, OnInit } from '@angular/core';
import {HttpClient,HttpParams} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

import {ApiRequestService} from '../api-request.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  tweet:any={};
  comment:any={};
  id:number;

  constructor(
    private http: HttpClient,
    private route:ActivatedRoute,
    private apiRquest:ApiRequestService
  ) {}

  ngOnInit(): void {
    this.getId();
  }

  getId(){
    this.id=+this.route.snapshot.paramMap.get("id");
    this.getComments();
    this.getTweet();
  }

  getComments(){
      this.apiRquest.get("tweet/comments/"+this.id,new HttpParams())
      .subscribe(
        res=>{this.comment=res.body;}
      )
  }

  getTweet(){
    this.apiRquest.get("tweet/"+this.id,new HttpParams())
    .subscribe(
      res=>{
        this.tweet=res.body;
      }
    )
  }

}
