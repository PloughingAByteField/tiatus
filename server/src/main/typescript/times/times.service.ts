import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Race } from '../races/race.model';

import { TimesHttpService } from './times-http.service';
import { RaceTimesSubject } from './race-times-subject.model';
import { PositionTime } from './postion-time.model';

@Injectable()
export class TimesService {
    protected raceEntries: RaceTimesSubject[]
        = new Array<RaceTimesSubject>();

    constructor(protected service: TimesHttpService) {}

    public getTimesForRace(race: Race): BehaviorSubject<PositionTime[]> {
        const subject: RaceTimesSubject
            = this.raceEntries.filter((s: RaceTimesSubject) => {
                if (s.race.id === race.id) {
                    return true;
                }
                return false;
            }).shift();

        if (subject) {
            return subject.subject;
        } else {
            const raceTimesSubject: RaceTimesSubject = new RaceTimesSubject();
            raceTimesSubject.race = race;
            this.raceEntries.push(raceTimesSubject);
            this.service.getTimesForRace(race).subscribe((times: PositionTime[]) => {
                if (times != null) {
                    raceTimesSubject.times = times;
                    raceTimesSubject.subject.next(raceTimesSubject.times);
                }
            });
            return raceTimesSubject.subject;
        }
    }

    public refreshForRace(race: Race): void {
        const subject: RaceTimesSubject
                = this.raceEntries.filter((s: RaceTimesSubject) => s.race.id === race.id).shift();
        if (subject) {
            this.service.getTimesForRace(race).subscribe((times: PositionTime[]) => {
                if (times != null) {
                    subject.times = times;
                    subject.subject.next(subject.times);
                }
            });
        }
    }
}
