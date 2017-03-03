import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Entry } from './entry.model';
import { RaceEntriesSubject } from './race-entries-subject.model';
import { Race } from '../races/race.model';

import { EntriesHttpService } from './entries-http.service';

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
        this.service.getEntries().subscribe((entries: Entry[]) => {
            this.entries = entries;
            this.subject.next(this.entries);
        });
    }

    public refreshForRace(race: Race): void {
        let subject: RaceEntriesSubject
                = this.raceEntries.filter((s: RaceEntriesSubject) => s.race.id === race.id).shift();

        if (subject) {
            this.service.getEntriesForRace(race).subscribe((entries: Entry[]) => {
                subject.entries = entries;
                subject.subject.next(subject.entries);
                subject.subject.complete();
            });
        }
    }

    public getEntriesForRace(race: Race): BehaviorSubject<Entry[]> {
        let subject: RaceEntriesSubject
            = this.raceEntries.filter((s: RaceEntriesSubject) => {
                if (s.race.id === race.id) {
                    return;
                }
            }).shift();

        if (subject) {
            return subject.subject;
        } else {
            // create new RaceEntriesSubject
            let raceEntriesSubject: RaceEntriesSubject = new RaceEntriesSubject();
            raceEntriesSubject.race = race;
            this.raceEntries.push(raceEntriesSubject);
            this.service.getEntriesForRace(race).subscribe((entries: Entry[]) => {
                raceEntriesSubject.entries = entries;
                raceEntriesSubject.subject.next(raceEntriesSubject.entries);
                raceEntriesSubject.subject.complete();
            });
            return raceEntriesSubject.subject;
        }
    }
}
