import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Entry, convertObjectToEntry } from '../../entries/entry.model';

import { EntriesHttpService } from '../../entries/entries-http.service';

@Injectable()
export class AdminEntriesHttpService extends EntriesHttpService {

    private headers = new Headers({ 'Content-Type': 'application/json' });
    private endPoint: string = '/rest/entries';

    constructor(protected http: Http) {
        super(http);
    }

    public createEntry(entry: Entry): Promise<Entry> {
        return this.http
            .post(this.endPoint,
            JSON.stringify(entry), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    let location: string = res.headers.get('location');
                    let locationParts = location.split('/');
                    let id: number = +locationParts[locationParts.length - 1];
                    entry.id = id;
                }
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }

    public removeEntry(entry: Entry): Promise<Entry> {
        return this.http
            .delete(this.endPoint + '/' + entry.id)
            .toPromise()
            .then(() => {
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateEntry(entry: Entry): Promise<Entry> {
        return this.http
            .put(this.endPoint + '/' + entry.id,
            JSON.stringify(entry), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }
}
