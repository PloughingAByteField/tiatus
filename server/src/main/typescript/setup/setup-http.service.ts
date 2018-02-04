import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import { User } from '../admin/users/user.model';

@Injectable()
export class SetupHttpService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    private endPoint: string = '/rest/setup/user';

    constructor(protected http: HttpClient) { }

    public createUser(user: User): Promise<User> {
        return this.http
            .post(this.endPoint,
            JSON.stringify(user), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    user.id = id;
                }
                return user;
            })
            .catch((err) => Promise.reject(err));
    }

}
