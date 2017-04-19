import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';

import { TranslateService } from '@ngx-translate/core';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { User } from '../admin/users/user.model';
import { LoginService } from './login.service';

@Component({
  selector: 'login',
  styleUrls: [
    './login.component.css'
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;

  constructor(
    private translate: TranslateService,
    private fb: FormBuilder,
    private service: LoginService
  ) {}

  public ngOnInit() {
    this.translate.setDefaultLang('en');
    this.translate.use('en');
    this.loginForm = this.fb.group({
      name: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250)
      ]),
      password: this.fb.control('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(250)
      ])
    });
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    console.log(value);
    let user: User = new User();
    user.userName = value.name;
    user.password = value.password;
    this.service.loginUser(user).then((redirectTo: string) => {
      if (redirectTo !== null) {
        window.location.href = redirectTo;
      }
    });
  }

}
