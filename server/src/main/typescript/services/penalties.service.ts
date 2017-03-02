import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Penalty } from '../models/penalty.model';

import { PenaltiesHttpService } from '../http-services/penalties.service';

@Injectable()
export class PenaltiesService {
    protected penalties: Penalty[] = new Array<Penalty>();
    protected penaltiesSubject: BehaviorSubject<Penalty[]>
        = new BehaviorSubject<Penalty[]>(this.penalties);

    constructor(protected service: PenaltiesHttpService) {
        this.service.getPenalties().subscribe((penalties: Penalty[]) => {
            this.penalties = penalties;
            this.penaltiesSubject.next(this.penalties);
        });
    }

    public getPenalties(): BehaviorSubject<Penalty[]> {
        return this.penaltiesSubject;
    }

}
