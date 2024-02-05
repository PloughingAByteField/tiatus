import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ClubsHttpService } from '../../clubs/clubs-http.service';

@Injectable()
export class ResultsHttpClubsService extends ClubsHttpService {

    constructor(protected http: HttpClient) {
        super(http);
    }
}
