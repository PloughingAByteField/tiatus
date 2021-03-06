import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { User } from '../admin/users/user.model';

import { LoginHttpService } from './login-http.service';

@Injectable()
export class LoginService {
    constructor(private service: LoginHttpService) {}

    public loginUser(user: User): Observable<string> {
        return this.service.loginUser(user);
    }
}
