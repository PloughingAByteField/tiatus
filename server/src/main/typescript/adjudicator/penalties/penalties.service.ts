import { Injectable } from '@angular/core';

import { Penalty } from '../../penalties/penalty.model';
import { Entry } from '../../entries/entry.model';

import { PenaltiesService } from '../../penalties/penalties.service';
import { AdjudicatorHttpPenaltiesService } from './penalties-http.service';

@Injectable()
export class AdjudicatorPenaltiesService extends PenaltiesService {

    constructor(protected service: AdjudicatorHttpPenaltiesService) {
        super(service);
    }

    public createPenalty(penalty: Penalty): void {
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

    public getPenaltiesForEntry(entry: Entry): Penalty[] {
        let penalties: Penalty[];
        if (this.penalties) {
            penalties = this.penalties.filter((penalty: Penalty) => penalty.entry === entry.id);
        }

        return penalties;
    }
}
