import { Component, OnInit } from '@angular/core';
import {HttpClient,HttpParams} from '@angular/common/http'
import {ActivatedRoute} from '@angular/router';
import {Router} from '@angular/router';

import {ApiRequestService} from '../api-request.service';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  tweets:any={};
  id:number;
  // baseUrl:string='http://10.20.20.76:8080/';
  baseUrl:string='http://restapi-env.eba-xd2trzjb.us-east-2.elasticbeanstalk.com/';
  selected:string;
  tweetText:string;
  user:any={};

  constructor(
    private http: HttpClient,
    private route:ActivatedRoute,
    private apiRquest:ApiRequestService,
    private router:Router
  ) { }

  ngOnInit(): void {
    console.log(this.router.url);
    let arr=this.router.url.split('/');
    this.getId();
    this.selected=arr[arr.length-1];
    this.getUserInfo();
    
  }


  getId(){
    this.id= +this.route.snapshot.paramMap.get("id");
    console.log(this.id);
  }

  getUserInfo(){
    this.apiRquest.get(this.baseUrl+"users/"+this.id,new HttpParams())
        .subscribe(res=>{
          this.user=res.body;
        })
  }

  click(barName:string){
    this.selected=barName;
  }

  createTweet(){
    let data={
      author: this.id,
      text:this.tweetText,
    }
    
    this.apiRquest.post(this.baseUrl+"tweet",data)
        .subscribe(res=>{
            if(res.status===200){
              console.log(this.router.url);
              this.selected='tweets';
              console.log(this.selected);
              if(this.router.url=='/user/'+this.id+'/tweets'){
                window.location.reload();
              }
              else{
                this.router.navigate(['/user',this.id,'tweets']);
              }
            //   this.router.navigateByUrl('/login', { skipLocationChange: true }).then(() => {
            //     this.router.navigate(['/user',this.id,'tweets']);
            // }); 

            }
        }
        )
  }
}
