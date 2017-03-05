import { Subject } from 'rxjs/Subject';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { EntryTime, mergeEntriesIntoEntryTimes } from './entry-time.model';
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
    private entryTimes: EntryTime[] = new Array<EntryTime>();

    constructor(
        public race: Race,
        protected entriesService: EntriesService,
        protected timesService: TimesService) {
            if (this.entriesService !== null && this.timesService !== null) {
                this.entriesService.getEntriesForRace(this.race).subscribe((entries: Entry[]) => {
                    this.entries = entries;
                    this.entryTimesForRace
                        = mergeEntriesIntoEntryTimes(this.entries, this.entryTimes);
                    this.entryTimesSubject.next(this.entryTimesForRace);
                });
                this.timesService.getTimesForRace(this.race)
                    .subscribe((entryTimes: EntryTime[]) => {
                        this.entryTimes = entryTimes;
                        this.entryTimesForRace
                            = mergeEntriesIntoEntryTimes(this.entries, this.entryTimes);
                        this.entryTimesSubject.next(this.entryTimesForRace);
                });
        }
    }
}
