import { EventPosition, convertObjectToEventPosition } from './event-positions.model';

export class Event {
    public name: string;
    public weighted: boolean;
    public positions: EventPosition[];
    public id: number;

    public sortPositionsByOrder(): void {
        if (this.positions) {
            this.positions.sort((ep1: EventPosition, ep2: EventPosition) => {
                if (ep1.positionOrder < ep2.positionOrder) {
                    return -1;
                }
                if (ep1.positionOrder > ep2.positionOrder) {
                    return 1;
                }
                return 0;
            });
        }
    }
}

export function convertObjectToEvent(obj: any): Event {
    const event: Event = new Event();
    event.name = obj.name;
    event.weighted = obj.weighted;
    event.id = obj.id;
    event.positions = new Array<EventPosition>();
    obj.positions.map((eventPosition: EventPosition) => {
      event.positions.push(convertObjectToEventPosition(eventPosition));
    });
    event.sortPositionsByOrder();
    return event;
}
