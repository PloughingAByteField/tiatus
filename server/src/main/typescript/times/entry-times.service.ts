import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';

import { EntriesService } from '../entries/entries.service';
import { TimesService } from '../times/times.service';

import { EntryTime } from './entry-time.model';
import { Entry } from '../entries/entry.model';
import { Race } from '../races/race.model';
import { PositionTime } from './postion-time.model';

import { RaceEntryTimes } from './race-entry-times.model';

@Injectable()
export class EntryTimesService {
    private raceEntries: RaceEntryTimes[] = new Array<RaceEntryTimes>();

    constructor(protected entriesService: EntriesService, protected timesService: TimesService) {}

    public refreshForRace(race: Race): void {
        this.timesService.refreshForRace(race);
        this.entriesService.refreshForRace(race);
    }

    public getEntriesForRace(race: Race): Subject<EntryTime[]> {
        if (race) {
            for (const entry of this.raceEntries) {
                if (entry.race.id === race.id) {
                    return entry.entryTimesSubject;
                }
            }
        }

        if (race) {
            const raceEntryTimes: RaceEntryTimes
                = new RaceEntryTimes(race, this.entriesService, this.timesService);
            this.raceEntries.push(raceEntryTimes);
            return raceEntryTimes.entryTimesSubject;
        }

        return null;
    }
}
