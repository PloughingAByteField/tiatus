
import { Club } from './club.model';
import { Race } from './race.model';
import { Event } from './event.model';

export class Entry {
    public crew: string;
    public name: string;
    public number: number;
    public raceOrder: number;
    public timeOnly: boolean;
    public clubs: Club[];
    public race: Race;
    public raceName: string;
    public event: Event;
    public id: number;
}
