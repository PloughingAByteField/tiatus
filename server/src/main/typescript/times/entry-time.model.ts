import { Entry } from '../entries/entry.model';
import { PositionTime } from './postion-time.model';

export class EntryTime {
    public entry: Entry;
    public times: PositionTime[];
}

export function mergeEntriesIntoEntryTimes(entries: Entry[], entryTimes: EntryTime[]): EntryTime[] {
    const merged: EntryTime[] = new Array<EntryTime>();
    entries.forEach((entry: Entry) => {
        const entryTime: EntryTime = new EntryTime();
        const entryTimeForEntry: EntryTime = entryTimes
            .filter((et: EntryTime) => {
                if (et.entry.id === entry.id) {
                    return true;
                }
                return false;
            }).shift();
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

export function mergePositionTimesIntoEntryTimes(entries: Entry[], positionTimes: PositionTime[]): EntryTime[] {
    const merged: EntryTime[] = new Array<EntryTime>();
    entries.forEach((entry: Entry) => {
        const entryTime: EntryTime = new EntryTime();
        const positionTimeForEntry: PositionTime = positionTimes
            .filter((pt: PositionTime) => {
                console.log(pt);
                console.log(entry);
                if (pt.entry === entry.id) {
                    return true;
                }
                return false;
            }).shift();
        if (positionTimeForEntry) {
            if (entryTime.times === undefined || entryTime.times == null) {
                entryTime.times = new Array<PositionTime>();
            }
            entryTime.times.push(positionTimeForEntry);
        } else {
            entryTime.times = new Array<PositionTime>();
        }
        entryTime.entry = entry;
        merged.push(entryTime);
    });
    return merged;
}
