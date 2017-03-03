import { Injectable } from '@angular/core';

import { Entry } from '../../models/entry.model';

import { SwapEntriesHttpService } from './swap-entries-http.service';

@Injectable()
export class SwapEntriesService {

    constructor(private service: SwapEntriesHttpService) {}

    public swapEntries(from: Entry, to: Entry): void {
        this.service.swapEntries(from, to);
    }
}
