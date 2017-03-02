import { Injectable } from '@angular/core';

import { Penalty } from '../../models/penalty.model';

import { PenaltiesService } from '../../services/penalties.service';
import { AdjudicatorHttpPenaltiesService } from '../http-services/penalties.service';

@Injectable()
export class AdjudicatorPenaltiesService extends PenaltiesService {

    constructor(protected service: AdjudicatorHttpPenaltiesService) {
        super(service);
    }

    public createPenalty(penalty: Penalty): void {
        console.log(this.penalties);
        this.service.createPenalty(penalty).then(() => {
            this.penalties.push(penalty);
            this.penaltiesSubject.next(this.penalties);
        });
    }

    public removePenalty(penalty: Penalty): void {
        this.service.removePenalty(penalty).then(() => {
            let index = this.penalties.indexOf(penalty);
            let sliced = this.penalties.splice(index, 1);
            this.penaltiesSubject.next(this.penalties);
        });
    }

    public updatePenalty(penalty: Penalty): void {
        this.service.updatePenalty(penalty).then();
    }
}
