import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { PositionsHttpService } from '../../positions/positions-http.service';

@Injectable()
export class ResultsHttpPositionsService extends PositionsHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

}
