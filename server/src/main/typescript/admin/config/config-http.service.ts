import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { ConfigHttpService } from '../../config/config-http.service';

@Injectable()
export class AdminConfigHttpService extends ConfigHttpService {

    constructor(protected http: Http) {
        super(http);
    }
}
