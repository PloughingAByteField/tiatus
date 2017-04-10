import { EventPosition, convertObjectToEventPosition } from './event-positions.model';

export class Event {
    public name: string;
    public weighted: boolean;
    public positions: EventPosition[];
    public id: number;
}

export function convertObjectToEvent(obj: any): Event {
    let event: Event = new Event();
    event.name = obj.name;
    event.weighted = obj.weighted;
    event.id = obj.id;
    event.positions = new Array<EventPosition>();
    obj.positions.map((eventPosition: EventPosition) => {
      event.positions.push(convertObjectToEventPosition(eventPosition));
    });
    return event;
}
