
export class Entry {
    public crew: string;
    public number: number;
    public raceOrder: number;
    public timeOnly: boolean;
    public clubs: number[];
    public race: number;
    public event: number;
    public id: number;
}

export function convertObjectToEntry(json: Entry): Entry {
    let entry: Entry = new Entry();
    entry.clubs = new Array<number>();
    json.clubs.forEach((club: number) => entry.clubs.push(club));
    entry.number = json.number;
    entry.crew = json.crew;
    entry.id = json.id;
    entry.event = json.event;
    entry.race = json.race;
    entry.raceOrder = json.raceOrder;
    entry.timeOnly = json.timeOnly;
    return entry;
}
