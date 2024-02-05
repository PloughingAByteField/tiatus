import { Entry } from './entry.model';
import { Race } from '../races/race.model';

import { BehaviorSubject } from 'rxjs';

export class RaceEntriesSubject {
    public race: Race;
    public entries: Entry[] = new Array<Entry>();

    public subject: BehaviorSubject<Entry[]>
        = new BehaviorSubject<Entry[]>(this.entries);

    public setEntriesForRace(entries: Entry[]): void {
        this.entries = entries;
        this.subject.next(this.entries);
    }    
}
