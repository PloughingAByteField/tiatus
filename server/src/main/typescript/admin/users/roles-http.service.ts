import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';

import { Role, convertObjectToRole } from './role.model';

@Injectable()
export class AdminRolesHttpService {

    private headers = new Headers({ 'Content-Type': 'application/json' });
    private endPoint: string = '/rest/users/roles';

    constructor(protected http: Http) { }

    public getRoles(): Observable<Role[]> {
        return this.http.get(this.endPoint)
            .map((response) => {
                return convertResponseToRoles(response);
            })
            .share();
    }

}

function convertResponseToRoles(response: Response): Role[] {
    const objectArray: Role[] = response.json();
    const roles: Role[] = new Array<Role>();
    objectArray.map((object: Role) => {
        roles.push(convertObjectToRole(object));
    });
    return roles;
}
