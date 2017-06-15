import { Injectable } from '@angular/core';

import { Entry } from '../../entries/entry.model';

import { Data } from '../model/data.model';

import { DisqualificationService } from '../../disqualification/disqualification.service';
import { ResultsHttpDisqualificationsService } from './disqualification-http.service';

import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

@Injectable()
export class ResultsDisqualificationService extends DisqualificationService {

    constructor(protected service: ResultsHttpDisqualificationsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getDisqualificationsData().subscribe((data: Data) => {
            if (!data.cached) {
                this.disqualifications = data.data;
                this.disqualificationsSubject.next(this.disqualifications);
            }
        });
    }
}
