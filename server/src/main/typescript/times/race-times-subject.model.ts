import { EntryTime } from '../models/entry-time.model';
import { Race } from '../models/race.model';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

export class RaceTimesSubject {
    public race: Race;
    public times: EntryTime[] = new Array<EntryTime>();
    public subject: BehaviorSubject<EntryTime[]>
        = new BehaviorSubject<EntryTime[]>(this.times);
}
