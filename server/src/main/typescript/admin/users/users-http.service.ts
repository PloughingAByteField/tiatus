import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';

import { User, convertObjectToUser } from './user.model';

@Injectable()
export class AdminUsersHttpService {

    private headers = new Headers({ 'Content-Type': 'application/json' });
    private endPoint: string = '/rest/users';

    constructor(protected http: Http) { }

    public getUsers(): Observable<User[]> {
        return this.http.get(this.endPoint)
            .map((response) => {
                return convertResponseToUsers(response);
            })
            .share();
    }

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
            JSON.stringify(user), { headers: this.headers })
            .toPromise()
            .then(() => {
                return user;
            })
            .catch((err) => Promise.reject(err));
    }
}

function convertResponseToUsers(response: Response): User[] {
    const objectArray: User[] = response.json();
    const users: User[] = new Array<User>();
    objectArray.map((object: User) => {
        users.push(convertObjectToUser(object));
    });
    return users;
}
