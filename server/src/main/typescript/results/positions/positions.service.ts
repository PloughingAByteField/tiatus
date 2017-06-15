import { Injectable } from '@angular/core';

import { Entry } from '../../entries/entry.model';

import { Data } from '../model/data.model';

import { PositionsService } from '../../positions/positions.service';
import { ResultsHttpPositionsService } from './positions-http.service';

import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

@Injectable()
export class ResultsPositionsService extends PositionsService {

    constructor(protected service: ResultsHttpPositionsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getPositionsData().subscribe((data: Data) => {
            if (!data.cached) {
                this.positions = data.data;
                this.subject.next(this.positions);
            }
        });
    }
}
