import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { EntryTime } from './entry-time.model';
import { Race } from '../races/race.model';

export class RaceTimesSubject {
    public race: Race;
    public times: EntryTime[] = new Array<EntryTime>();
    public subject: BehaviorSubject<EntryTime[]>
        = new BehaviorSubject<EntryTime[]>(this.times);
}
