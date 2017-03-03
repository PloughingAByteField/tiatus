import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';

@Injectable()
export class ConfigHttpService {
    protected endpoint: string = '/tiatus/config/config.json';

    constructor(private http: Http) {}

    public getConfig(): Observable<Object> {
        return this.http.get(this.endpoint)
            .map((res: Response) =>  res.json()).share();
    }
}
