import { Injectable } from '@angular/core';

import { ClubsService } from '../../clubs/clubs.service';
import { AdminClubsHttpService } from './clubs-http.service';

@Injectable()
export class AdminClubsService extends ClubsService {

    constructor(protected service: AdminClubsHttpService) {
        super(service);
    }

}
