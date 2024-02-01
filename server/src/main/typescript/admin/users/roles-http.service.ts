import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

import { CachedHttpService } from '../../http/cached-http.service';
import { Data } from '../../model/data.model';

@Injectable()
export class AdminRolesHttpService extends CachedHttpService {

    private endPoint: string = '/rest/users/roles';

    constructor(protected http: HttpClient) {
        super(http);
     }

    public getRoles(): Observable<Data> {
        return this.getData(this.endPoint);
    }

}
