import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { Role } from './role.model';

@Injectable()
export class AdminRolesHttpService  {

    private endPoint: string = '/rest/users/roles';

    private httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(protected http: HttpClient) {}

    public getRoles(): Observable<Role[]> {
        return this.http.get<Role[]>(this.endPoint, this.httpOptions);
    }

}
