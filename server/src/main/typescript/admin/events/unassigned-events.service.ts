import { Injectable } from '@angular/core';

import { UnassignedEventsService } from '../../unassigned-events/unassigned-events.service';
import { AdminUnassignedEventsHttpService } from './unassigned-events-http.service';

@Injectable()
export class AdminUnassignedEventsService extends UnassignedEventsService {

    constructor(protected service: AdminUnassignedEventsHttpService) {
        super(service);
        this.refresh();
    }

}
