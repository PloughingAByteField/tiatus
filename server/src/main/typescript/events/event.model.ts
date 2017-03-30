import { EventPosition, convertObjectToEventPosition } from './event-positions.model';

export class Event {
    public name: string;
    public weighted: boolean;
    public positions: EventPosition[];
    public id: number;
}

export function convertObjectToEvent(json: Event): Event {
    let event: Event = new Event();
    event.name = json.name;
    event.weighted = json.weighted;
    event.id = json.id;
    event.positions = new Array<EventPosition>();
    json.positions.map((eventPosition: EventPosition) => {
      event.positions.push(convertObjectToEventPosition(eventPosition));
    });
    return event;
}
