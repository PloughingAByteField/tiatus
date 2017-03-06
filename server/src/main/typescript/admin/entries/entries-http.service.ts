import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { EntriesHttpService } from '../../entries/entries-http.service';

@Injectable()
export class AdminEntriesHttpService extends EntriesHttpService {

    constructor(protected http: Http) {
        super(http);
    }
}
