import { Entry } from '../entries/entry.model';
import { Position } from './position.model';
import { Race } from './race.model';
import { PositionTime } from './postion-time.model';

export class EntryTime {
    public entry: Entry;
    public times: PositionTime[];
}
