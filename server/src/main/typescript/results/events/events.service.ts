import { Injectable } from '@angular/core';

import { Entry } from '../../entries/entry.model';

import { Data } from '../model/data.model';

import { EventsService } from '../../events/events.service';
import { ResultsHttpEventsService } from './events-http.service';

import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

@Injectable()
export class ResultsEventsService extends EventsService {

    constructor(protected service: ResultsHttpEventsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getEventsData().subscribe((data: Data) => {
            let updateData: boolean = false;
            if (data.cached) {
                if (data.data.length !== this.events.length) {
                    updateData = true;
                }
            } else {
                updateData = true;
            }
            if (updateData) {
                this.events = data.data;
                this.subject.next(this.events);
            }
        });
    }
}
