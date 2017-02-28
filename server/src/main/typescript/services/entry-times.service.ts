import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';

import { EntriesService } from './entries.service';
import { TimesService } from './times.service';

import { EntryTime } from '../models/entry-time.model';
import { Position } from '../models/position.model';
import { Entry } from '../models/entry.model';
import { Race } from '../models/race.model';
import { PositionTime } from '../models/postion-time.model';

import { RaceEntryTimes } from '../models/race-entry-times.model';

@Injectable()
export class EntryTimesService {
    public  entryTimesSubject: Subject<EntryTime[]> =
        new BehaviorSubject<EntryTime[]>(new Array<EntryTime>());
    private raceEntries: RaceEntryTimes[] = new Array<RaceEntryTimes>();

    constructor(private entriesService: EntriesService, private timesService: TimesService) {}

    public getEntriesForRace(race: Race): Subject<EntryTime[]> {
        if (race) {
            for (let entry of this.raceEntries) {
                if (entry.race.id === race.id) {
                    this.entryTimesSubject.next(entry.entries);
                    return this.entryTimesSubject;
                }
            }
        }

        if (race) {
            let raceEntryTimes: RaceEntryTimes = new RaceEntryTimes();
            raceEntryTimes.race = race;
            raceEntryTimes.entries = new Array<EntryTime>();
            let entriesObs = this.entriesService.getEntriesForRace(race);
            let entryTimesObs = this.timesService.getTimesForRace(race);
            Observable.forkJoin(entriesObs, entryTimesObs).subscribe((data) => {
                    let entries: Entry[] = data[0];
                    let entryTimes: EntryTime[] = data[1];
                    entries.forEach((entry: Entry) => {
                        let found: boolean = false;
                        entryTimes.forEach((entryTime: EntryTime) => {
                            if (entryTime.entry.id === entry.id
                                && entryTime.entry.event === undefined) {
                                entryTime.entry = entry;
                                raceEntryTimes.entries.push(entryTime);
                                found = true;
                                return;
                            }
                        });
                        if (!found) {
                            let entryTime: EntryTime = new EntryTime();
                            entryTime.entry = entry;
                            entryTime.times = new Array<PositionTime>();
                            raceEntryTimes.entries.push(entryTime);
                        }
                    });
                    this.entryTimesSubject.next(raceEntryTimes.entries);
            });
            this.raceEntries.push(raceEntryTimes);
        }

        return this.entryTimesSubject;
    }
}
