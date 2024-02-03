import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { EntryTime } from './entry-time.model';
import { Race } from '../races/race.model';
import { PositionTime } from './postion-time.model';

export class RaceTimesSubject {
    public race: Race;
    public times: PositionTime[] = new Array<PositionTime>();
    public subject: BehaviorSubject<PositionTime[]>
        = new BehaviorSubject<PositionTime[]>(this.times);
}
