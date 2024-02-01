import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { CachedHttpService } from '../http/cached-http.service';
import { Data } from '../model/data.model';

@Injectable()
export class ConfigHttpService extends CachedHttpService {
    protected endPoint: string = '/rest/config/config';

    constructor(protected http: HttpClient) {
        super(http);
    }

    public getConfig(): Observable<Data> {
        return this.getData(this.endPoint);
    }
}
