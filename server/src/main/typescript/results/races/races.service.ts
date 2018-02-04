import { Injectable } from '@angular/core';

import { Data } from '../../model/data.model';

import { RacesService } from '../../races/races.service';
import { ResultsHttpRacesService } from './races-http.service';

@Injectable()
export class ResultsRacesService extends RacesService {

    constructor(protected service: ResultsHttpRacesService) {
        super(service);
    }

    public refresh(): void {
        this.service.getRacesData().subscribe((data: Data) => {
            if (!data.cached) {
                this.races = data.data;
                this.racesSubject.next(this.races);
            }
        });
    }
}
