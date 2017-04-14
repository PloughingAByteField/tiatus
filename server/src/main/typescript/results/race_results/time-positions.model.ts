import { EntryTime } from '../../times/entry-time.model';
import { Position } from '../../positions/position.model';

export class TimesPositions {
    public times: EntryTime[] = new Array<EntryTime>();
    public finish: Position;
    public start: Position;
}
