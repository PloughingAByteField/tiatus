import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { ClubsHttpService } from '../../clubs/clubs-http.service';

@Injectable()
export class AdminClubsHttpService extends ClubsHttpService {

    constructor(protected http: Http) {
        super(http);
    }
}
