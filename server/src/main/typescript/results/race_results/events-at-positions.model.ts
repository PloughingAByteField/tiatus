import { Event } from '../../events/event.model';
import { Position } from '../../positions/position.model';

export class EventsAtPositions {
    public events: Event[] = new Array<Event>();
    public finish: Position;
    public start: Position;
}
