import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import { User } from '../admin/users/user.model';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class LoginHttpService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
    private endPoint: string = '/rest/login';

    private location: string = null;
    private locationObservable: BehaviorSubject<string> = new BehaviorSubject<string>(this.location);

    constructor(protected http: HttpClient) { }

    public loginUser(user: User): Observable<string> {
        const formData: string = 'user=' + user.userName + '&pwd=' + user.password;
        this.http
            .post<string>(this.endPoint, formData, { observe: 'response', headers: this.headers })
            .subscribe((res: HttpResponse<string>) => {
                if (res.status === 202) {
                    this.location = res.headers.get('location');
                    this.locationObservable.next(this.location);
                }
            }, (err: HttpErrorResponse) => {
                console.log(err);
            }

        );
        return this.locationObservable;
    }

}
