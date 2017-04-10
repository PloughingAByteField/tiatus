
export class Entry {
    public crew: string;
    public number: number;
    public raceOrder: number;
    public fixedNumber: boolean;
    public timeOnly: boolean;
    public clubs: number[];
    public race: number;
    public event: number;
    public id: number;
}

export function convertObjectToEntry(obj: any): Entry {
    let entry: Entry = new Entry();
    entry.clubs = new Array<number>();
    obj.clubs.forEach((club: number) => entry.clubs.push(club));
    entry.number = obj.number;
    entry.crew = obj.crew;
    entry.id = obj.id;
    entry.event = obj.event;
    entry.race = obj.race;
    entry.raceOrder = obj.raceOrder;
    entry.timeOnly = obj.timeOnly;
    entry.fixedNumber = obj.fixedNumber;
    return entry;
}
