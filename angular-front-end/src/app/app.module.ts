import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';


import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { UserComponent } from './user/user.component';
import { RegisterComponent } from './register/register.component';
import { TweestlistComponent } from './tweestlist/tweestlist.component';
import { MyTweetsComponent } from './my-tweets/my-tweets.component';
import { MyProfileComponent } from './my-profile/my-profile.component';
import { MyFollowerComponent } from './my-follower/my-follower.component';
import { CommentComponent } from './comment/comment.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UserComponent,
    RegisterComponent,
    TweestlistComponent,
    MyTweetsComponent,
    MyProfileComponent,
    MyFollowerComponent,
    CommentComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
