import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Race } from '../models/race.model';

import { RacesHttpService } from '../http-services/races.service';

@Injectable()
export class RacesService {
    protected races: Race[] = new Array<Race>();
    protected racesSubject: BehaviorSubject<Race[]>
        = new BehaviorSubject<Race[]>(this.races);

    constructor(protected service: RacesHttpService) {
        this.service.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            this.racesSubject.next(this.races);
        });
    }

    public getRaceForId(id: number): Race {
      return this.races.filter((race: Race) => race.id === id).shift();
    }

    public getRaces(): BehaviorSubject<Race[]> {
        return this.racesSubject;
    }

}
