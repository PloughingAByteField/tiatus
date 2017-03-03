import { Entry } from './entry.model';
import { Race } from '../models/race.model';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

export class RaceEntriesSubject {
    public race: Race;
    public entries: Entry[] = new Array<Entry>();
    public subject: BehaviorSubject<Entry[]>
        = new BehaviorSubject<Entry[]>(this.entries);
}
