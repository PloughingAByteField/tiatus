import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Race } from '../../races/race.model';

@Injectable()
export class SelectedRaceService {
    private selected: BehaviorSubject<Race> = new BehaviorSubject<Race>(null);

    public get getSelectedRace(): BehaviorSubject<Race> {
        return this.selected;
    }

    public set setSelectedRace(race: Race) {
        console.log('seleting selected to ' + race.id);
        this.selected.next(race);
    }
}
