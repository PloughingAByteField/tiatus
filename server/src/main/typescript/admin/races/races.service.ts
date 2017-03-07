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
        this.service.createRace(race).then((r: Race) => {
            this.races.push(r);
            this.racesSubject.next(this.races);
        });
    }

    public removeRace(race: Race): void {
        this.service.removeRace(race).then((r: Race) => {
            let index = this.races.indexOf(r);
            let sliced = this.races.splice(index, 1);
            this.racesSubject.next(this.races);
        });
    }

    public updateRace(race: Race): void {
        this.service.updateRace(race).then();
    }
}
