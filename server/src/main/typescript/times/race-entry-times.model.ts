import { Subject } from 'rxjs/Subject';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { EntryTime, mergePositionTimesIntoEntryTimes } from './entry-time.model';
import { Entry } from '../entries/entry.model';
import { Race } from '../races/race.model';
import { PositionTime } from '../times/postion-time.model';

import { EntriesService } from '../entries/entries.service';
import { TimesService } from '../times/times.service';

export class RaceEntryTimes {
    public entryTimesForRace: EntryTime[] = new Array<EntryTime>();
    public entryTimesSubject: Subject<EntryTime[]> =
        new BehaviorSubject<EntryTime[]>(this.entryTimesForRace);

    private entries: Entry[] = new Array<Entry>();
    private positionTimes: PositionTime[] = new Array<PositionTime>();

    constructor(
        public race: Race,
        protected entriesService: EntriesService,
        protected timesService: TimesService) {
            if (this.entriesService !== null && this.timesService !== null) {
                this.entriesService.getEntriesForRace(this.race).subscribe((entries: Entry[]) => {
                    this.entries = entries;
                    this.entryTimesForRace
                        = mergePositionTimesIntoEntryTimes(this.entries, this.positionTimes);
                    this.entryTimesSubject.next(this.entryTimesForRace);
                });
                this.timesService.getTimesForRace(this.race)
                    .subscribe((positionTimes: PositionTime[]) => {
                        this.positionTimes = positionTimes;
                        this.entryTimesForRace
                            = mergePositionTimesIntoEntryTimes(this.entries, this.positionTimes);
                        this.entryTimesSubject.next(this.entryTimesForRace);
                });
        }
    }
}
