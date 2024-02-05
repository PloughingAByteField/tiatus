import { Injectable } from '@angular/core';

import { RacesService } from '../../races/races.service';
import { Race } from '../../races/race.model';

import { ResultsHttpRacesService } from './races-http.service';


@Injectable()
export class ResultsRacesService extends RacesService {

    constructor(protected service: ResultsHttpRacesService) {
        super(service);
    }

    public refresh(): void {
        this.service.getRaces().subscribe((races: Race[]) => {
            if (races != null) {
                this.races = races;
                this.racesSubject.next(this.races);
            }
        });
    }
}
