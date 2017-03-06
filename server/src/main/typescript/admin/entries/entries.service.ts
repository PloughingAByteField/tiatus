import { Injectable } from '@angular/core';

import { EntriesService } from '../../entries/entries.service';
import { AdminEntriesHttpService } from './entries-http.service';

@Injectable()
export class AdminEntriesService extends EntriesService {

    constructor(protected service: AdminEntriesHttpService) {
        super(service);
    }

}
