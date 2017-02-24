export class Event {
    public name: string;
    public finishingPosition: number;
    public startingPosition: number;
    public id: number;
}

export function convertJsonToEvent(json: Event): Event {
    let event: Event = new Event();
    event.name = json.name;
    event.id = json.id;
    event.finishingPosition = json.finishingPosition;
    event.startingPosition = json.startingPosition;
    return event;
}
