import { Injectable } from '@angular/core';

import { Disqualification } from '../../disqualification/disqualification.model';

import { DisqualificationService } from '../../disqualification/disqualification.service';

import { AdjudicatorHttpDisqualificationsService } from './disqualification-http.service';

@Injectable()
export class AdjudicatorDisqualificationService extends DisqualificationService {

    constructor(protected service: AdjudicatorHttpDisqualificationsService) {
        super(service);
    }

    public disqualify(disqualification: Disqualification): void {
        this.service.disqualify(disqualification).then(() => {
            this.disqualifications.push(disqualification);
            this.penaltiesSubject.next(this.disqualifications);
        });
    }

    public removeDisqualification(disqualification: Disqualification): void {
        this.service.removeDisqualification(disqualification).then(() => {
            let index = this.disqualifications.indexOf(disqualification);
            let sliced = this.disqualifications.splice(index, 1);
            this.penaltiesSubject.next(this.disqualifications);
        });
    }

    public updateDisqualification(disqualification: Disqualification): void {
        this.service.updateDisqualification(disqualification).then();
    }
}
