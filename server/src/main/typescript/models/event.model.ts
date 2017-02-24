export class Event {
    public name: string;
    public id: number;
}

export function convertJsonToEvent(json: Event): Event {
    let event: Event = new Event();
    event.name = json.name;
    event.id = json.id;
    return event;
}
