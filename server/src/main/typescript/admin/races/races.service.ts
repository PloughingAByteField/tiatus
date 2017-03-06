import { Injectable } from '@angular/core';

import { RacesService } from '../../races/races.service';
import { AdminRacesHttpService } from './races-http.service';

@Injectable()
export class AdminRacesService extends RacesService {

    constructor(protected service: AdminRacesHttpService) {
        super(service);
    }
}
