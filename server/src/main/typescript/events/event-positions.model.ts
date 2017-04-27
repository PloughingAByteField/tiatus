export class EventPosition {
    public event: number;
    public position: number;
    public positionOrder: number;
    public id: number;
}

export function convertObjectToEventPosition(json: EventPosition): EventPosition {
    const event: EventPosition = new EventPosition();
    event.event = json.event;
    event.id = json.id;
    event.position = json.position;
    event.positionOrder = json.positionOrder;
    return event;
}
