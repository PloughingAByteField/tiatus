import { Injectable } from '@angular/core';

import { Race } from '../../races/race.model';

import { TimesService } from '../../times/times.service';
import { TimesFullHttpService } from './times-full-http.service';

@Injectable()
export class TimesFullService extends TimesService {

    constructor(protected service: TimesFullHttpService) {
        super(service);
    }

}
