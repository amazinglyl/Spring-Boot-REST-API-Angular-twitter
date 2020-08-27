import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule,Routes} from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import {RegisterComponent} from './register/register.component';
import { TweestlistComponent } from './tweestlist/tweestlist.component';
import { MyTweetsComponent } from './my-tweets/my-tweets.component';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { MyFollowerComponent } from './my-follower/my-follower.component';
import { CommentComponent } from './comment/comment.component';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { 
    path: 'user/:id', 
    component: UserComponent,
    children: [
      {path: 'home', component: TweestlistComponent},
      {path: 'tweets', component: MyTweetsComponent},
      {path: 'profile', component: MyProfileComponent},
      {path: 'followers', component: MyFollowerComponent},
      {path: 'comment/:id', component: CommentComponent},
    ]
  
  },
  { path: 'register', component: RegisterComponent}
];


@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
