import { Component, OnInit } from '@angular/core';
import {HttpParams} from '@angular/common/http';

import {ApiRequestService} from '../api-request.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(
    private apiRquest:ApiRequestService
  ) { }

  ngOnInit(): void {
  }

}
