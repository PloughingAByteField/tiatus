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
            let updateData: boolean = false;
            if (data.cached) {
                if (data.data.length !== this.disqualifications.length) {
                    updateData = true;
                }
            } else {
                updateData = true;
            }
            if (updateData) {
                this.disqualifications = data.data;
                this.disqualificationsSubject.next(this.disqualifications);
            }
        });
    }
}
