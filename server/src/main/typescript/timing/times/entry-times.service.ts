import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';

import { EntriesService } from '../../entries/entries.service';
import { TimingTimesService } from './times.service';

import { EntryTime, mergeEntriesIntoEntryTimes } from '../../times/entry-time.model';
import { Position } from '../../positions/position.model';
import { Entry } from '../../entries/entry.model';
import { Race } from '../../races/race.model';
import { PositionTime } from '../../times/postion-time.model';

import { RaceEntryTimes } from './race-entry-times.model';
import { EntryTimesService } from '../../times/entry-times.service';

@Injectable()
export class TimingEntryTimesService extends EntryTimesService {

    constructor(
        protected entriesService: EntriesService,
        protected timesService: TimingTimesService) {
        super(entriesService, timesService);
    }

    public addTimeForPosition(
        position: Position, timeStamp: number, positionTime: PositionTime, entry: Entry): void {
        if (positionTime.time === undefined || positionTime.time !== timeStamp) {
            positionTime.synced = false;
            if (positionTime.time === undefined) {
                positionTime.time = timeStamp;
                this.timesService.setTimeForEntryAtPosition(
                    entry, position, positionTime)
                    .then()
                    .catch((err) => console.log(err));

            } else if (positionTime.time !== timeStamp) {
                positionTime.time = timeStamp;
                this.timesService.updateTimeForEntryAtPosition(
                    entry, position, positionTime)
                    .then()
                    .catch((err) => console.log(err));
            }
        }
    }

    public getTimesForPositionInRace(position: Position, race: Race): Subject<EntryTime[]> {
        let entries: Entry[];
        let times: EntryTime[] = new Array<EntryTime>();
        let subject: Subject<EntryTime[]> = new BehaviorSubject<EntryTime[]>(times);
        this.entriesService.getEntriesForRace(race).subscribe((entriesData: Entry[]) => {
            entries = entriesData;
            this.timesService.getTimesForPositionInRace(position, race)
            .subscribe((data: EntryTime[]) => {
                times = data;
                if (entries) {
                    times = mergeEntriesIntoEntryTimes(entries, times);
                    subject.next(times);
                }
            });
        });
        return subject;
    }
}
