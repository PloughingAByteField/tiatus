import { EventEmitter, Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { EntryTime } from './entry-time.model';
import { Race } from '../races/race.model';

import { TimesHttpService } from './times-http.service';
import { RaceTimesSubject } from './race-times-subject.model';

@Injectable()
export class TimesService {
    protected raceEntries: RaceTimesSubject[]
        = new Array<RaceTimesSubject>();

    constructor(protected service: TimesHttpService) {}

    public getTimesForRace(race: Race): BehaviorSubject<EntryTime[]> {
        let subject: RaceTimesSubject
            = this.raceEntries.filter((s: RaceTimesSubject) => {
                if (s.race.id === race.id) {
                    return;
                }
            }).shift();

        if (subject) {
            return subject.subject;
        } else {
            let raceTimesSubject: RaceTimesSubject = new RaceTimesSubject();
            raceTimesSubject.race = race;
            this.raceEntries.push(raceTimesSubject);
            this.service.getTimesForRace(race).subscribe((times: EntryTime[]) => {
                raceTimesSubject.times = times;
                raceTimesSubject.subject.next(raceTimesSubject.times);
            });
            return raceTimesSubject.subject;
        }
    }

    public refreshForRace(race: Race): void {
        let subject: RaceTimesSubject
                = this.raceEntries.filter((s: RaceTimesSubject) => s.race.id === race.id).shift();
        if (subject) {
            this.service.getTimesForRace(race).subscribe((times: EntryTime[]) => {
                subject.times = times;
                subject.subject.next(subject.times);
            });
        }
    }
}
