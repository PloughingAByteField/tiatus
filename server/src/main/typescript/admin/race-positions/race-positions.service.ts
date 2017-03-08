import { Injectable } from '@angular/core';

import { ClubsService } from '../../clubs/clubs.service';
import { RacePositionsHttpService } from './race-positions-http.service';

@Injectable()
export class RacePositionsService extends ClubsService {

    constructor(protected service: RacePositionsHttpService) {
        super(service);
    }

}
