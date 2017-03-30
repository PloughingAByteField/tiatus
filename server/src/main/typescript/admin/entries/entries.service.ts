import { Injectable } from '@angular/core';

import { Entry } from '../../entries/entry.model';

import { EntriesService } from '../../entries/entries.service';
import { AdminEntriesHttpService } from './entries-http.service';

@Injectable()
export class AdminEntriesService extends EntriesService {

    constructor(protected service: AdminEntriesHttpService) {
        super(service);
    }

    public createEntry(event: Entry): Promise<Entry> {
        return new Promise((resolve) => this.service.createEntry(event).then((e: Entry) => {
            this.entries.push(e);
            this.subject.next(this.entries);
            resolve(e);
        }));
    }

    public removeEntry(entry: Entry): void {
        this.service.removeEntry(entry).then((e: Entry) => {
            let index = this.entries.indexOf(entry);
            this.entries.splice(index, 1);
            this.subject.next(this.entries);
        });
    }

    public updateEntry(entry: Entry): Promise<Entry> {
        return new Promise((resolve) => this.service.updateEntry(entry).then((e: Entry) => {
            this.subject.next(this.entries);
            resolve(e);
        }));
    }
}
