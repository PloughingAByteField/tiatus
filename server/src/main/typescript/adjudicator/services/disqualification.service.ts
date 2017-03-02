import { Injectable } from '@angular/core';

import { Disqualification } from '../../models/disqualification.model';

import { DisqualificationService } from '../../services/disqualification.service';

import { AdjudicatorHttpDisqualificationsService }
    from '../http-services/disqualifications.service';

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
