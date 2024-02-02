import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import { User, convertObjectToUser } from './user.model';

import { CachedHttpService } from '../../http/cached-http.service';
import { Data } from '../../model/data.model';
import { subscribeOn } from 'rxjs/operators';

@Injectable()
export class AdminUsersHttpService extends CachedHttpService {

    private endPoint: string = '/rest/users';

    private httpHeader = {
        observe: 'response' as const,
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(protected http: HttpClient) {
        super(http);
    }

    public getUsers(): Observable<Data> {
        return this.getData(this.endPoint);
    }

    public createUser(user: User): Promise<User> {
        return this.http
            .post(this.endPoint, 
                user,
                this.httpHeader)
            .toPromise()
            .then((res: HttpResponse<User>) => {
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

    public removeUser(user: User): Promise<User> {
        return this.http
            .delete(this.endPoint + '/' + user.id)
            .toPromise()
            .then(() => {
                return user;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateUser(user: User): Promise<User> {
        return this.http
            .put(this.endPoint + '/' + user.id,
                user,
                this.httpHeader)
            .toPromise()
            .then(() => {
                return user;
            })
            .catch((err) => Promise.reject(err));
    }
}
