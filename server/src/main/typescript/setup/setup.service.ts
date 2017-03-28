import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { User } from '../admin/users/user.model';

import { SetupHttpService } from './setup-http.service';

@Injectable()
export class SetupService {
    constructor(private service: SetupHttpService) {}

    public createUser(user: User): Promise<User> {
        return new Promise((resolve) => this.service.createUser(user).then((createdUser: User) => {
            resolve(createdUser);
        }));
    }
}
