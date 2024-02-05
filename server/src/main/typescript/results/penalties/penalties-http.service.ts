import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { PenaltiesHttpService } from '../../penalties/penalties-http.service';

@Injectable()
export class ResultsHttpPenaltiesService extends PenaltiesHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }

}
