import { Injectable } from '@angular/core';

import { Race } from '../../races/race.model';

import { RacesService } from '../../races/races.service';
import { AdjudicatorRacesHttpService } from './races-http.service';

@Injectable()
export class AdjudicatorRacesService extends RacesService {

    constructor(protected service: AdjudicatorRacesHttpService) {
        super(service);
    }

    public updateRace(race: Race): void {
        this.service.updateRace(race).then();
    }
}
