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
            // reorganise race order
            let updatedRaces = this.races.filter((raceToUpdate: Race) => {
                if (raceToUpdate.raceOrder > r.raceOrder) {
                    raceToUpdate.raceOrder = raceToUpdate.raceOrder - 1;
                    return raceToUpdate;
                }
            });
            updatedRaces.forEach((updatedRace: Race) => this.updateRace(updatedRace));

            this.races.sort((a: Race, b: Race) => {
                if (a.raceOrder < b.raceOrder) {
                    return -1;
                } else if (a.raceOrder === b.raceOrder) {
                    return 0;
                } else if (a.raceOrder > b.raceOrder) {
                    return 1;
                }
            });
            this.racesSubject.next(this.races);
        });
    }

    public updateRace(race: Race): void {
        this.service.updateRace(race).then();
    }

    public getRaceForId(id: number): Race {
        for (let race of this.races) {
            if (race.id === id) {
                return race;
            }
        }
        return null;
    }
}
