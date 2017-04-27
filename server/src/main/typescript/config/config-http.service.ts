import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';

@Injectable()
export class ConfigHttpService {
    protected endpoint: string = '/tiatus/config/config.json';

    constructor(protected http: Http) {}

    public getConfig(): Observable<object> {
        return this.http.get(this.endpoint)
            .map((res: Response) =>  res.json()).share();
    }
}
