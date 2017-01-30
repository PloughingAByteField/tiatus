
import { Club } from './club.model';
import { Race } from './race.model';

export class Entry {
    crew: string;
    name: string;
    number: number;
    raceOrder: number;
    timeOnly: boolean;
    clubs: Club[];
    race: Race;
    raceName: string;
    id: number;
}