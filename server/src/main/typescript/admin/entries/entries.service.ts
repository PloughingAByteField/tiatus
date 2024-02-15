import { Injectable } from '@angular/core';

import { Entry } from '../../entries/entry.model';

import { EntriesService } from '../../entries/entries.service';
import { AdminEntriesHttpService } from './entries-http.service';
import { BehaviorSubject } from 'rxjs';
import { FileProcessingResult } from './FileProcessingResult.model';

@Injectable()
export class AdminEntriesService extends EntriesService {

    protected draw: BehaviorSubject<string> = new BehaviorSubject<string>(null);
    
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

    public removeEntry(entry: Entry): Promise<Entry> {
        return new Promise((resolve) => this.service.removeEntry(entry).then((e: Entry) => {
            const index = this.entries.indexOf(entry);
            this.entries.splice(index, 1);
            this.subject.next(this.entries);
            resolve(e);
        }));
    }

    public updateEntry(entry: Entry): Promise<Entry> {
        return new Promise((resolve) => this.service.updateEntry(entry).then((e: Entry) => {
            this.subject.next(this.entries);
            resolve(e);
        }));
    }

    public updateEntries(entries: Entry[]): void {
        this.service.updateEntries(entries).then((e: Entry[]) => {
            this.subject.next(this.entries);
        });
    }

    public uploadDraw(file: File): Promise<FileProcessingResult> {
        return new Promise((resolve) => this.service.uploadDraw(file).then((status: FileProcessingResult) => {
            console.log(status);
            if (status != null) {
                this.draw.next(status.data);
                resolve(status);
            }
        }));
    }
}
