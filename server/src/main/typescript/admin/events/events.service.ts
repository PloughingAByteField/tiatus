import { Injectable } from '@angular/core';

import { EventsService } from '../../events/events.service';
import { AdminEventsHttpService } from './events-http.service';

@Injectable()
export class AdminEventsService extends EventsService {

    constructor(protected service: AdminEventsHttpService) {
        super(service);
    }

}
