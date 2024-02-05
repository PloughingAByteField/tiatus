import { Injectable } from '@angular/core';

import { DisqualificationService } from '../../disqualification/disqualification.service';
import { ResultsHttpDisqualificationsService } from './disqualification-http.service';

import { Disqualification } from '../../disqualification/disqualification.model';

@Injectable()
export class ResultsDisqualificationService extends DisqualificationService {

    constructor(protected service: ResultsHttpDisqualificationsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getDisqualifications().subscribe((disqualifications: Disqualification[]) => {
            if (disqualifications != null) {
                this.disqualifications = disqualifications;
                this.disqualificationsSubject.next(this.disqualifications);
            }
        });
    }
}
