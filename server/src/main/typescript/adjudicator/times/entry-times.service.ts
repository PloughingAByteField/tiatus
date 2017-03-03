import { Injectable } from '@angular/core';

import { EntriesService } from '../../entries/entries.service';
import { TimesFullService } from './times.service';

import { EntryTimesService } from '../../times/entry-times.service';

@Injectable()
export class EntryTimesFullService extends EntryTimesService {

    constructor(
        protected entriesService: EntriesService,
        protected timesService: TimesFullService) {
            super(entriesService, timesService);
        }
}
