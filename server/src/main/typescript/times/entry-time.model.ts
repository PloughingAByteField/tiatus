import { Entry } from '../entries/entry.model';
import { PositionTime } from './postion-time.model';

export class EntryTime {
    public entry: Entry;
    public times: PositionTime[];
}

export function mergeEntriesIntoEntryTimes(entries: Entry[], entryTimes: EntryTime[]): EntryTime[] {
    let merged: EntryTime[] = new Array<EntryTime>();
    entries.forEach((entry: Entry) => {
        let entryTime: EntryTime = new EntryTime();
        let entryTimeForEntry: EntryTime = entryTimes
            .filter((et: EntryTime) => et.entry.id === entry.id).shift();
        if (entryTimeForEntry) {
            entryTime.times = entryTimeForEntry.times;
        } else {
            entryTime.times = new Array<PositionTime>();
        }
        entryTime.entry = entry;
        merged.push(entryTime);
    });
    return merged;
}
