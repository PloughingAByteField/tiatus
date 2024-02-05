import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { DisqualificationHttpService } from '../../disqualification/disqualification-http.service';

@Injectable()
export class ResultsHttpDisqualificationsService extends DisqualificationHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }
}
