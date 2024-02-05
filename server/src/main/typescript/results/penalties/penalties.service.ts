import { Injectable } from '@angular/core';

import { PenaltiesService } from '../../penalties/penalties.service';
import { ResultsHttpPenaltiesService } from './penalties-http.service';
import { Penalty } from '../../penalties/penalty.model';

@Injectable()
export class ResultsPenaltiesService extends PenaltiesService {

    constructor(protected service: ResultsHttpPenaltiesService) {
        super(service);
    }

    public refresh(): void {
        this.service.getPenalties().subscribe((penalties: Penalty[]) => {
            if (penalties != null) {
                this.penalties = penalties;
                this.penaltiesSubject.next(this.penalties);
            }
        });
    }
}
