import { Injectable } from '@angular/core';

import { Entry } from '../../entries/entry.model';

import { Data } from '../model/data.model';

import { ClubsService } from '../../clubs/clubs.service';
import { ResultsHttpClubsService } from './clubs-http.service';

import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

@Injectable()
export class ResultsClubsService extends ClubsService {

    constructor(protected service: ResultsHttpClubsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getClubsData().subscribe((data: Data) => {
            let updateData: boolean = false;
            if (data.cached) {
                if (data.data.length !== this.clubs.length) {
                    updateData = true;
                }
            } else {
                updateData = true;
            }
            if (updateData) {
                this.clubs = data.data;
                this.subject.next(this.clubs);
            }
        });
    }
}
