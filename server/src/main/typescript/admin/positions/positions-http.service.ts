import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { PositionsHttpService } from '../../positions/positions-http.service';

@Injectable()
export class AdminPositionsHttpService extends PositionsHttpService {

    constructor(protected http: Http) {
        super(http);
    }
}
