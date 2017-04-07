export class Race {
    public name: string;
    public active: boolean;
    public closed: boolean;
    public drawLocked: boolean;
    public raceOrder: number;
    public startTime: string;
    public id: number;
}

export function convertObjectToRace(json: any): Race {
    let race: Race = new Race();
    race.name = json.name;
    race.active = json.active;
    race.closed = json.closed;
    race.drawLocked = json.drawLocked;
    race.raceOrder = json.raceOrder;
    race.startTime = json.startTime;
    race.id = json.id;
    return race;
}
