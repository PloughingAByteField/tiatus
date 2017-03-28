import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { User } from '../admin/users/user.model';
import { SetupService } from './setup.service';

@Component({
  selector: 'setup',
  styleUrls: [
    './setup.component.css'
  ],
  templateUrl: './setup.component.html'
})
export class SetupComponent implements OnInit {
  public logo = '/assets/img/stopwatch.svg';
  public tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';
  public addUserForm: FormGroup;

  constructor(
    private translate: TranslateService,
    private fb: FormBuilder,
    private service: SetupService,
    private router: Router
  ) {
    translate.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');
  }

  public ngOnInit() {
    this.addUserForm = this.fb.group({
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
    this.service.createUser(user).then((createdUser: User) => this.router.navigateByUrl('/'));
  }

}
