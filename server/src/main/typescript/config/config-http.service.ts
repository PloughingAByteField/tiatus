import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable()
export class ConfigHttpService {
    protected endPoint: string = '/rest/config/config';

    protected httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(protected http: HttpClient) {}

    public getConfig(): Observable<object> {
        return this.http.get<object>(this.endPoint, this.httpOptions);
    }
}
