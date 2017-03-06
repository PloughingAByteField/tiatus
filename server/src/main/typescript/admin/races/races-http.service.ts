import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { RacesHttpService } from '../../races/races-http.service';

@Injectable()
export class AdminRacesHttpService extends RacesHttpService {

    constructor(protected http: Http) {
        super(http);
    }
}
