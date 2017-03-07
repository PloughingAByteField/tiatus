import { Injectable } from '@angular/core';

import { Race } from '../../races/race.model';
import { RacesService } from '../../races/races.service';
import { AdminRacesHttpService } from './races-http.service';

@Injectable()
export class AdminRacesService extends RacesService {

    constructor(protected service: AdminRacesHttpService) {
        super(service);
    }

    public addRace(race: Race): void {
        this.races.push(race);
        this.racesSubject.next(this.races);
    }
}
