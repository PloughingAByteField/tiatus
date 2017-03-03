import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';

import { Entry } from '../../models/entry.model';

@Injectable()
export class SwapEntriesHttpService {

    private endpoint: string = '/rest/entries/swapEntries';

    constructor(private http: Http) {}

    public swapEntries(from: Entry, to: Entry): Promise<Entry> {
        console.log('Swap from ' + from.number + ' to ' + to.number);
        return this.http.post(this.endpoint + '/' + from.id + '/' + to.id, {})
      .toPromise()
      .then((res: Response) => {
        return from;
      })
      .catch((err) => Promise.reject(err));
    }
}
