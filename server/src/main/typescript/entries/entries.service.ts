import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { Entry, convertObjectToEntry } from './entry.model';
import { RaceEntriesSubject } from './race-entries-subject.model';
import { Race } from '../races/race.model';
import { Data } from '../model/data.model';

import { EntriesHttpService } from './entries-http.service';

import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';

@Injectable()
export class EntriesService {
    protected entries: Entry[] = new Array<Entry>();
    protected subject: BehaviorSubject<Entry[]>
        = new BehaviorSubject<Entry[]>(this.entries);
    protected raceEntries: RaceEntriesSubject[]
        = new Array<RaceEntriesSubject>();

    constructor(protected service: EntriesHttpService) {
        this.refresh();
    }

    public getEntries(): BehaviorSubject<Entry[]> {
        return this.subject;
    }

    public refresh(): void {
        this.service.getEntries().subscribe((data: Data) => {
            if (data.data !== undefined) {
                this.entries = data.data;
                this.subject.next(this.entries);
            }
        });
    }

    public refreshForRace(race: Race): void {
        const subject: RaceEntriesSubject
                = this.raceEntries.filter((s: RaceEntriesSubject) => s.race.id === race.id).shift();

        if (subject) {
            this.service.getEntriesForRace(race).subscribe((data: Data) => {
                subject.entries = data.data;
                subject.subject.next(subject.entries);
            });
        }
    }

    public getEntriesForRace(race: Race): BehaviorSubject<Entry[]> {
        const subject: RaceEntriesSubject
            = this.raceEntries.filter((s: RaceEntriesSubject) => {
                if (s.race.id === race.id) {
                    return true;
                }
                return false;
            }).shift();

        if (subject) {
            return subject.subject;

        } else {
            // create new RaceEntriesSubject
            const raceEntriesSubject: RaceEntriesSubject = new RaceEntriesSubject();
            raceEntriesSubject.race = race;
            this.raceEntries.push(raceEntriesSubject);
            this.service.getEntriesForRace(race).subscribe((data: Data) => {
                raceEntriesSubject.entries = data.data;
                raceEntriesSubject.subject.next(raceEntriesSubject.entries);
            });
            return raceEntriesSubject.subject;
        }
    }

    public processMessage(message: Message): void {
        console.log('process message');
        console.log(message);
        const entry: Entry = convertObjectToEntry(message.data);
        console.log(entry);
        if (message.type === MessageType.ADD) {
            this.entries.push(entry);

        } else if (message.type === MessageType.DELETE) {
            const deletedEntry: Entry = this.getEntryForId(entry.id);
            if (deletedEntry !== null) {
                const index = this.entries.indexOf(deletedEntry);
                const sliced = this.entries.splice(index, 1);
            }

        } else if (message.type === MessageType.UPDATE) {
            const updatedEntry: Entry = this.getEntryForId(entry.id);
            if (updatedEntry !== null) {
                updatedEntry.clubs = entry.clubs;
                updatedEntry.crew = entry.crew;
                updatedEntry.event = entry.event;
                updatedEntry.fixedNumber = entry.fixedNumber;
                updatedEntry.number = entry.number;
                updatedEntry.race = entry.race;
                updatedEntry.raceOrder = entry.raceOrder;
                updatedEntry.timeOnly = entry.timeOnly;
            }
        }

        this.subject.next(this.entries);
    }

    public getEntryForId(entryId: number): Entry {
        for (const entry of this.entries) {
            if (entry.id === entryId) {
                return entry;
            }
        }
        return null;
    }
}
