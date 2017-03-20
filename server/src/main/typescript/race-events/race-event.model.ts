export class RaceEvent {
    public event: number;
    public race: number;
    public raceEventOrder: number;
    public id: number;
}

export function convertJsonToRaceEvent(json: RaceEvent): RaceEvent {
    let raceEvent: RaceEvent = new RaceEvent();
    raceEvent.event = json.event;
    raceEvent.id = json.id;
    raceEvent.race = json.race;
    raceEvent.raceEventOrder = json.raceEventOrder;
    return raceEvent;
}
