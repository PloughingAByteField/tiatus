import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';

import { User } from '../admin/users/user.model';

@Injectable()
export class LoginHttpService {

    private headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' });
    private endPoint: string = '/rest/login';

    constructor(protected http: Http) { }

    public loginUser(user: User): Promise<string> {
        const formData: string = 'user=' + user.userName + '&pwd=' + user.password;
        return this.http
            .post(this.endPoint, formData, { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 202) {
                    return res.headers.get('location');
                }
                return null;
            })
            .catch((err) => Promise.reject(err));
    }

}
