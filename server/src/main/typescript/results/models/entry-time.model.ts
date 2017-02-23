import { Entry } from '../../models/entry.model';
import { Position } from '../../models/position.model';
import { Race } from '../../models/race.model';
import { PositionTime } from '../../models/postion-time.model';

export class EntryTime {
    public entry: Entry;
    public times: PositionTime[];
}
