
import { Club, convertJsonToClub } from '../clubs/club.model';
import { Race, convertJsonToRace } from '../races/race.model';
import { Event, convertJsonToEvent } from '../models/event.model';

export class Entry {
    public crew: string;
    public number: number;
    public raceOrder: number;
    public timeOnly: boolean;
    public clubs: Club[];
    public race: Race;
    public event: Event;
    public id: number;

    public getClubNames(): string {
        return this.clubs.map((club) => club.clubName).join(' / ');
    }
}

export function convertJsonToEntry(json: Entry): Entry {
    let entry: Entry = new Entry();
    entry.clubs = new Array<Club>();
    json.clubs.forEach((club: Club) => entry.clubs.push(convertJsonToClub(club)));
    entry.number = json.number;
    entry.crew = json.crew;
    entry.id = json.id;
    entry.event = convertJsonToEvent(json.event);
    entry.race = convertJsonToRace(json.race);
    entry.raceOrder = json.raceOrder;
    entry.timeOnly = json.timeOnly;
    return entry;
}
